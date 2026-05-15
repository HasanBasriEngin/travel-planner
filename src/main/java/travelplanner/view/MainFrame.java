package travelplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import travelplanner.decorator.BaseCityPlan;
import travelplanner.decorator.CityCenterVisitDecorator;
import travelplanner.decorator.MuseumVisitDecorator;
import travelplanner.decorator.ParkVisitDecorator;
import travelplanner.decorator.PlannableCity;
import travelplanner.decorator.ShoppingMallVisitDecorator;
import travelplanner.iterator.CityIterator;
import travelplanner.iterator.WeatherFilteredCollection;
import travelplanner.model.City;
import travelplanner.model.WeatherState;
import travelplanner.observer.WeatherObserver;
import travelplanner.observer.WeatherReportProvider;
import travelplanner.repository.CityRepository;
import travelplanner.strategy.AreaSortStrategy;
import travelplanner.strategy.NameSortStrategy;
import travelplanner.strategy.PopulationSortStrategy;
import travelplanner.strategy.SortStrategy;

/**
 * Main application window for the Travel Planner System.
 */
public class MainFrame extends JFrame implements WeatherObserver {

    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 820;
    private static final int MINIMUM_WIDTH = 760;
    private static final int MINIMUM_HEIGHT = 660;
    private static final int STACKED_LAYOUT_BREAKPOINT = 920;
    private static final double LISTS_SPLIT_WEIGHT = 0.62;
    private static final double CHARTS_SPLIT_WEIGHT = 0.78;
    private static final String SORT_BY_NAME = "Sort by Name";
    private static final String SORT_BY_POPULATION = "Sort by Population";
    private static final String SORT_BY_AREA = "Sort by Area";
    private static final String EMPTY_PLAN_MESSAGE =
            "No city selected. Select a city from the All Cities list to create a plan.";
    private static final int SECTION_SPACING = 10;

    private final DefaultListModel<City> allCitiesListModel;
    private final DefaultListModel<City> weatherCitiesListModel;
    private final JList<City> allCitiesList;
    private final JList<City> weatherCitiesList;
    private final JComboBox<String> sortOptionsComboBox;
    private final JComboBox<WeatherState> weatherFilterComboBox;
    private final JLabel allCitiesStatusLabel;
    private final JLabel weatherCitiesStatusLabel;
    private final JLabel selectedCityLabel;
    private final JLabel totalCostLabel;
    private final JLabel totalHoursLabel;
    private final JCheckBox museumVisitCheckBox;
    private final JCheckBox shoppingMallVisitCheckBox;
    private final JCheckBox parkVisitCheckBox;
    private final JCheckBox cityCenterVisitCheckBox;
    private final JTextArea planDescriptionTextArea;
    private final WeatherReportProvider weatherReportProvider;
    private final TemperatureChartPanel temperatureChartPanel;
    private final WeatherPieChartPanel weatherPieChartPanel;
    private JSplitPane listsSplitPane;
    private JSplitPane chartsSplitPane;
    private JPanel plannerCenterPanel;
    private JComponent plannerSelectedPanel;
    private JComponent plannerActivitiesPanel;
    private JComponent plannerSummaryPanel;
    private boolean refreshingAllCitiesList;
    private boolean responsiveLayoutInitialized;
    private boolean stackedLayout;
    private String selectedCityName;

    public MainFrame() {
        this.allCitiesListModel = new DefaultListModel<>();
        this.weatherCitiesListModel = new DefaultListModel<>();
        this.allCitiesList = new ResponsiveCityList(allCitiesListModel);
        this.weatherCitiesList = new ResponsiveCityList(weatherCitiesListModel);
        this.sortOptionsComboBox = new JComboBox<>(new String[]{
                SORT_BY_NAME,
                SORT_BY_POPULATION,
                SORT_BY_AREA
        });
        this.weatherFilterComboBox = new JComboBox<>(WeatherState.values());
        this.allCitiesStatusLabel = UiStyles.createMutedLabel("");
        this.weatherCitiesStatusLabel = UiStyles.createMutedLabel("");
        this.selectedCityLabel = UiStyles.createValueLabel("No city selected.");
        this.totalCostLabel = UiStyles.createValueLabel("-");
        this.totalHoursLabel = UiStyles.createValueLabel("-");
        this.museumVisitCheckBox = new JCheckBox("Museum Visit");
        this.shoppingMallVisitCheckBox = new JCheckBox("Shopping Mall Visit");
        this.parkVisitCheckBox = new JCheckBox("Park Visit");
        this.cityCenterVisitCheckBox = new JCheckBox("City Center Visit");
        this.planDescriptionTextArea = new JTextArea(4, 30);
        this.weatherReportProvider = new WeatherReportProvider();
        this.temperatureChartPanel = new TemperatureChartPanel();
        this.weatherPieChartPanel = new WeatherPieChartPanel();

        setTitle("Travel Planner System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(true);
        setContentPane(createRootPanel());
        setLocationRelativeTo(null);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        configureCityLists();
        configurePlannerComponents();

        sortOptionsComboBox.addActionListener(event -> refreshAllCitiesList());
        weatherFilterComboBox.addActionListener(event -> refreshWeatherCitiesList());
        museumVisitCheckBox.addActionListener(event -> updatePlannerDisplay());
        shoppingMallVisitCheckBox.addActionListener(event -> updatePlannerDisplay());
        parkVisitCheckBox.addActionListener(event -> updatePlannerDisplay());
        cityCenterVisitCheckBox.addActionListener(event -> updatePlannerDisplay());

        refreshAllLists();

        weatherReportProvider.attach(this);
        weatherReportProvider.attach(temperatureChartPanel);
        weatherReportProvider.attach(weatherPieChartPanel);
        weatherReportProvider.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                shutdown();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                updateResponsiveLayout();
            }
        });
        updateResponsiveLayout();
    }

    private JPanel createRootPanel() {
        JPanel rootPanel = new JPanel(new BorderLayout(0, 18));
        rootPanel.setBackground(UiStyles.APP_BACKGROUND);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        rootPanel.add(createTopControlCard(), BorderLayout.NORTH);
        rootPanel.add(createContentPanel(), BorderLayout.CENTER);
        return rootPanel;
    }

    private JPanel createTopControlCard() {
        JPanel topCard = new JPanel(new BorderLayout(24, 0));
        topCard.setOpaque(false);
        topCard.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Travel Planner System");
        titleLabel.setFont(UiStyles.headingFont());
        titleLabel.setForeground(UiStyles.TEXT_PRIMARY);

        JLabel subtitleLabel = UiStyles.createMutedLabel(
                "Compare destinations, watch live weather changes, and prepare activity budgets."
        );

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(createMetricStrip());

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        controlsPanel.setOpaque(false);
        controlsPanel.add(createControlGroup("Sort Cities", sortOptionsComboBox));
        controlsPanel.add(createControlGroup("Filter Weather", weatherFilterComboBox));

        topCard.add(titlePanel, BorderLayout.CENTER);
        topCard.add(controlsPanel, BorderLayout.EAST);
        return topCard;
    }

    private JPanel createMetricStrip() {
        JPanel metricStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        metricStrip.setOpaque(false);
        metricStrip.setAlignmentX(Component.LEFT_ALIGNMENT);
        metricStrip.add(createMetricPill(
                "Destinations",
                String.valueOf(CityRepository.getInstance().getCities().size()),
                UiStyles.ACCENT
        ));
        metricStrip.add(createMetricPill("Weather States", String.valueOf(WeatherState.values().length),
                UiStyles.SECONDARY_ACCENT));
        metricStrip.add(createMetricPill("Activity Types", "4", UiStyles.WARM_ACCENT));
        return metricStrip;
    }

    private JPanel createMetricPill(String labelText, String valueText, Color accentColor) {
        JPanel metricPanel = new JPanel(new BorderLayout(8, 0));
        metricPanel.setOpaque(false);
        metricPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLabel valueLabel = new JLabel(valueText);
        valueLabel.setFont(UiStyles.emphasisFont());
        valueLabel.setForeground(accentColor);

        JLabel textLabel = UiStyles.createMutedLabel(labelText);
        textLabel.setFont(UiStyles.smallFont());

        metricPanel.add(valueLabel, BorderLayout.WEST);
        metricPanel.add(textLabel, BorderLayout.CENTER);
        return metricPanel;
    }

    private JComponent createControlGroup(String labelText, JComboBox<?> comboBox) {
        JPanel controlGroup = new JPanel(new BorderLayout(0, 6));
        controlGroup.setOpaque(false);
        controlGroup.setPreferredSize(new Dimension(170, 58));
        controlGroup.setMinimumSize(new Dimension(170, 58));
        JLabel label = UiStyles.createLabel(labelText);
        label.setFont(UiStyles.smallEmphasisFont());
        label.setForeground(UiStyles.TEXT_SECONDARY);
        controlGroup.add(label, BorderLayout.NORTH);
        controlGroup.add(comboBox, BorderLayout.CENTER);
        return controlGroup;
    }

    private JScrollPane createContentPanel() {
        JPanel contentPanel = new ViewportWidthPanel(new GridBagLayout());
        contentPanel.setOpaque(true);
        contentPanel.setBackground(UiStyles.APP_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 0.35;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 0, SECTION_SPACING, 0);
        contentPanel.add(createListsSection(), constraints);

        constraints.gridy = 1;
        constraints.weighty = 0.42;
        contentPanel.add(createChartsSection(), constraints);

        constraints.gridy = 2;
        constraints.weighty = 0.23;
        constraints.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(createPlannerPanel(), constraints);

        JScrollPane contentScrollPane = new JScrollPane(contentPanel);
        contentScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentScrollPane.getViewport().setBackground(UiStyles.APP_BACKGROUND);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(18);
        return contentScrollPane;
    }

    private JSplitPane createListsSection() {
        JPanel allCitiesPanel = createAllCitiesPanel();
        JPanel weatherCitiesPanel = createWeatherCitiesPanel();
        allCitiesPanel.setMinimumSize(new Dimension(300, 230));
        weatherCitiesPanel.setMinimumSize(new Dimension(300, 230));
        listsSplitPane = createResponsiveSplitPane(allCitiesPanel, weatherCitiesPanel, LISTS_SPLIT_WEIGHT, 310);
        return listsSplitPane;
    }

    private JSplitPane createChartsSection() {
        temperatureChartPanel.setMinimumSize(new Dimension(520, 280));
        weatherPieChartPanel.setMinimumSize(new Dimension(300, 280));
        chartsSplitPane = createResponsiveSplitPane(temperatureChartPanel, weatherPieChartPanel, CHARTS_SPLIT_WEIGHT, 340);
        return chartsSplitPane;
    }

    private JSplitPane createResponsiveSplitPane(
            JComponent leftComponent,
            JComponent rightComponent,
            double resizeWeight,
            int preferredHeight
    ) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftComponent, rightComponent);
        splitPane.setOpaque(false);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(18);
        splitPane.setResizeWeight(resizeWeight);
        splitPane.setDividerLocation(resizeWeight);
        splitPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        splitPane.setPreferredSize(new Dimension(0, preferredHeight));
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                BasicSplitPaneDivider divider = new BasicSplitPaneDivider(this);
                divider.setBorder(BorderFactory.createEmptyBorder());
                divider.setBackground(UiStyles.APP_BACKGROUND);
                return divider;
            }
        });
        return splitPane;
    }

    private JPanel createAllCitiesPanel() {
        JPanel allCitiesPanel = UiStyles.createCardPanel();
        allCitiesPanel.add(createCardHeader("All Cities", allCitiesStatusLabel), BorderLayout.NORTH);
        allCitiesPanel.add(createAllCitiesScrollPane(), BorderLayout.CENTER);
        return allCitiesPanel;
    }

    private JPanel createWeatherCitiesPanel() {
        JPanel weatherCitiesPanel = UiStyles.createCardPanel();
        weatherCitiesPanel.add(createCardHeader("Cities by Weather", weatherCitiesStatusLabel), BorderLayout.NORTH);
        weatherCitiesPanel.add(createWeatherCitiesScrollPane(), BorderLayout.CENTER);
        return weatherCitiesPanel;
    }

    private JPanel createCardHeader(String title, JLabel statusLabel) {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 4));
        headerPanel.setOpaque(false);

        JLabel titleLabel = UiStyles.createSectionTitle(title);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(statusLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JScrollPane createAllCitiesScrollPane() {
        JScrollPane scrollPane = new JScrollPane(allCitiesList);
        UiStyles.styleScrollPane(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(320, 250));
        return scrollPane;
    }

    private JScrollPane createWeatherCitiesScrollPane() {
        JScrollPane scrollPane = new JScrollPane(weatherCitiesList);
        UiStyles.styleScrollPane(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(280, 250));
        return scrollPane;
    }

    private JPanel createPlannerPanel() {
        JPanel plannerPanel = UiStyles.createCardPanel();
        plannerPanel.add(createPlannerTitlePanel(), BorderLayout.NORTH);
        plannerPanel.add(createPlannerBodyPanel(), BorderLayout.CENTER);
        return plannerPanel;
    }

    private JPanel createPlannerTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout(0, 4));
        titlePanel.setOpaque(false);
        titlePanel.add(UiStyles.createSectionTitle("City Activity Planner"), BorderLayout.NORTH);
        titlePanel.add(UiStyles.createMutedLabel(
                "Activities, budget, and duration for the selected destination."
        ), BorderLayout.CENTER);
        return titlePanel;
    }

    private JPanel createPlannerBodyPanel() {
        JPanel plannerBodyPanel = new JPanel(new BorderLayout(0, 0));
        plannerBodyPanel.setOpaque(false);
        plannerBodyPanel.add(createPlannerCenterPanel(), BorderLayout.CENTER);
        return plannerBodyPanel;
    }

    private JPanel createPlannerSelectionPanel() {
        return UiStyles.createInfoPanel("Selected City", selectedCityLabel);
    }

    private JPanel createPlannerCenterPanel() {
        plannerCenterPanel = new JPanel(new GridBagLayout());
        plannerCenterPanel.setOpaque(false);
        plannerSelectedPanel = createPlannerSelectionPanel();
        plannerActivitiesPanel = createPlannerActivitiesPanel();
        plannerSummaryPanel = createPlannerSummaryPanel();
        layoutPlannerCenter(false);
        return plannerCenterPanel;
    }

    private void layoutPlannerCenter(boolean stacked) {
        if (plannerCenterPanel == null || plannerSelectedPanel == null
                || plannerActivitiesPanel == null || plannerSummaryPanel == null) {
            return;
        }

        plannerCenterPanel.removeAll();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;

        if (stacked) {
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1.0;
            constraints.weighty = 0.24;
            constraints.insets = new Insets(0, 0, 12, 0);
            plannerCenterPanel.add(plannerSelectedPanel, constraints);

            constraints.gridy = 1;
            constraints.weighty = 0.36;
            plannerCenterPanel.add(plannerActivitiesPanel, constraints);

            constraints.gridy = 2;
            constraints.weighty = 0.40;
            constraints.insets = new Insets(0, 0, 0, 0);
            plannerCenterPanel.add(plannerSummaryPanel, constraints);
        } else {
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 0.22;
            constraints.weighty = 1.0;
            constraints.insets = new Insets(0, 0, 0, 14);
            plannerCenterPanel.add(plannerSelectedPanel, constraints);

            constraints.gridx = 1;
            constraints.weightx = 0.34;
            constraints.insets = new Insets(0, 0, 0, 14);
            plannerCenterPanel.add(plannerActivitiesPanel, constraints);

            constraints.gridx = 2;
            constraints.weightx = 0.44;
            constraints.insets = new Insets(0, 0, 0, 0);
            plannerCenterPanel.add(plannerSummaryPanel, constraints);
        }

        plannerCenterPanel.revalidate();
        plannerCenterPanel.repaint();
    }

    private JPanel createPlannerActivitiesPanel() {
        JPanel activitiesGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        activitiesGrid.setOpaque(false);
        activitiesGrid.add(museumVisitCheckBox);
        activitiesGrid.add(shoppingMallVisitCheckBox);
        activitiesGrid.add(parkVisitCheckBox);
        activitiesGrid.add(cityCenterVisitCheckBox);

        return UiStyles.createInfoPanel("Activities", activitiesGrid);
    }

    private JPanel createPlannerSummaryPanel() {
        JPanel summaryPanel = new JPanel(new BorderLayout(0, 10));
        summaryPanel.setOpaque(true);
        summaryPanel.setBackground(UiStyles.PANEL_BACKGROUND);

        summaryPanel.add(createPlannerSummaryMetricsPanel(), BorderLayout.NORTH);
        summaryPanel.add(createPlannerDescriptionPanel(), BorderLayout.CENTER);
        return UiStyles.createInfoPanel("Plan Summary", summaryPanel);
    }

    private JPanel createPlannerSummaryMetricsPanel() {
        JPanel metricsPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        metricsPanel.setOpaque(true);
        metricsPanel.setBackground(UiStyles.PANEL_BACKGROUND);
        metricsPanel.add(createMetricSummaryPanel("Total Cost", totalCostLabel));
        metricsPanel.add(createMetricSummaryPanel("Total Required Hours", totalHoursLabel));
        return metricsPanel;
    }

    private JPanel createMetricSummaryPanel(String title, JLabel valueLabel) {
        JPanel metricPanel = new JPanel(new BorderLayout(0, 6));
        metricPanel.setOpaque(true);
        metricPanel.setBackground(UiStyles.PANEL_BACKGROUND);
        metricPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UiStyles.CARD_BORDER));

        JLabel titleLabel = UiStyles.createMutedLabel(title);
        titleLabel.setFont(UiStyles.smallEmphasisFont());
        valueLabel.setForeground(UiStyles.ACCENT_DARK);

        metricPanel.add(titleLabel, BorderLayout.NORTH);
        metricPanel.add(valueLabel, BorderLayout.CENTER);
        return metricPanel;
    }

    private JScrollPane createPlannerDescriptionPanel() {
        JScrollPane descriptionScrollPane = new JScrollPane(planDescriptionTextArea);
        UiStyles.styleScrollPane(descriptionScrollPane);
        descriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScrollPane.setPreferredSize(new Dimension(320, 70));
        return descriptionScrollPane;
    }

    private void configureCityLists() {
        allCitiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        weatherCitiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        allCitiesList.setCellRenderer(new CityListCellRenderer());
        weatherCitiesList.setCellRenderer(new CityListCellRenderer());
        allCitiesList.setFont(UiStyles.bodyFont());
        weatherCitiesList.setFont(UiStyles.bodyFont());
        allCitiesList.setBackground(UiStyles.CARD_BACKGROUND);
        weatherCitiesList.setBackground(UiStyles.CARD_BACKGROUND);
        allCitiesList.setSelectionBackground(UiStyles.ACCENT_SOFT);
        weatherCitiesList.setSelectionBackground(UiStyles.ACCENT_SOFT);
        allCitiesList.setFixedCellHeight(72);
        weatherCitiesList.setFixedCellHeight(72);
        allCitiesList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && !refreshingAllCitiesList) {
                City selectedCity = allCitiesList.getSelectedValue();
                selectedCityName = selectedCity != null ? selectedCity.getName() : null;
                updatePlannerDisplay();
            }
        });
    }

    private void configurePlannerComponents() {
        UiStyles.styleComboBox(sortOptionsComboBox);
        UiStyles.styleComboBox(weatherFilterComboBox);
        UiStyles.styleCheckBox(museumVisitCheckBox);
        UiStyles.styleCheckBox(shoppingMallVisitCheckBox);
        UiStyles.styleCheckBox(parkVisitCheckBox);
        UiStyles.styleCheckBox(cityCenterVisitCheckBox);
        UiStyles.styleTextArea(planDescriptionTextArea);
        planDescriptionTextArea.setText(EMPTY_PLAN_MESSAGE);
        setPlannerControlsEnabled(false);
    }

    private void refreshAllCitiesList() {
        refreshingAllCitiesList = true;

        try {
            String cityNameToRestore = selectedCityName;
            allCitiesListModel.clear();

            List<City> cities = CityRepository.getInstance().getCities();
            SortStrategy sortStrategy = getSelectedSortStrategy();
            List<City> sortedCities = sortStrategy.sort(cities);

            for (City city : sortedCities) {
                allCitiesListModel.addElement(city);
            }

            restoreSelectedCity(cityNameToRestore);
            allCitiesStatusLabel.setText(String.format(
                    "%d cities sorted by %s.",
                    sortedCities.size(),
                    sortOptionsComboBox.getSelectedItem()
            ));
        } finally {
            refreshingAllCitiesList = false;
        }

        updatePlannerDisplay();
    }

    private void refreshWeatherCitiesList() {
        weatherCitiesListModel.clear();

        List<City> cities = CityRepository.getInstance().getCities();
        WeatherFilteredCollection weatherFilteredCollection = new WeatherFilteredCollection(cities);
        CityIterator cityIterator = getSelectedWeatherIterator(weatherFilteredCollection);
        int visibleCityCount = 0;

        while (cityIterator.hasNext()) {
            weatherCitiesListModel.addElement(cityIterator.next());
            visibleCityCount++;
        }

        weatherCitiesStatusLabel.setText(String.format(
                "%d cities with %s weather.",
                visibleCityCount,
                weatherFilterComboBox.getSelectedItem()
        ));
    }

    private void refreshAllLists() {
        refreshAllCitiesList();
        refreshWeatherCitiesList();
    }

    private SortStrategy getSelectedSortStrategy() {
        String selectedOption = (String) sortOptionsComboBox.getSelectedItem();

        if (SORT_BY_POPULATION.equals(selectedOption)) {
            return new PopulationSortStrategy();
        }

        if (SORT_BY_AREA.equals(selectedOption)) {
            return new AreaSortStrategy();
        }

        return new NameSortStrategy();
    }

    private CityIterator getSelectedWeatherIterator(WeatherFilteredCollection weatherFilteredCollection) {
        WeatherState selectedWeatherState = (WeatherState) weatherFilterComboBox.getSelectedItem();

        if (selectedWeatherState == WeatherState.CLOUDY) {
            return weatherFilteredCollection.createCloudyIterator();
        }

        if (selectedWeatherState == WeatherState.RAINY) {
            return weatherFilteredCollection.createRainyIterator();
        }

        if (selectedWeatherState == WeatherState.SNOWY) {
            return weatherFilteredCollection.createSnowyIterator();
        }

        return weatherFilteredCollection.createSunnyIterator();
    }

    private void restoreSelectedCity(String cityNameToRestore) {
        if (cityNameToRestore == null) {
            allCitiesList.clearSelection();
            return;
        }

        for (int index = 0; index < allCitiesListModel.getSize(); index++) {
            City city = allCitiesListModel.getElementAt(index);
            if (city.getName().equals(cityNameToRestore)) {
                allCitiesList.setSelectedIndex(index);
                allCitiesList.ensureIndexIsVisible(index);
                selectedCityName = cityNameToRestore;
                return;
            }
        }

        allCitiesList.clearSelection();
        selectedCityName = null;
    }

    private void updatePlannerDisplay() {
        City selectedCity = allCitiesList.getSelectedValue();

        if (selectedCity == null) {
            selectedCityName = null;
            selectedCityLabel.setText("No city selected.");
            planDescriptionTextArea.setText(EMPTY_PLAN_MESSAGE);
            totalCostLabel.setText("-");
            totalHoursLabel.setText("-");
            setPlannerControlsEnabled(false);
            return;
        }

        selectedCityName = selectedCity.getName();
        PlannableCity plan = buildPlanForSelectedCity(selectedCity);
        selectedCityLabel.setText(String.format(
                "<html><b>%s</b><br/>%s weather | %.1f C</html>",
                selectedCity.getName(),
                formatWeatherState(selectedCity.getCurrentWeatherState()),
                selectedCity.getCurrentTemperature()
        ));
        planDescriptionTextArea.setText(plan.getDescription());
        totalCostLabel.setText(String.format("%.2f TL", plan.getTotalCost()));
        totalHoursLabel.setText(String.format("%.1f hours", plan.getTotalHours()));
        setPlannerControlsEnabled(true);
    }

    private String formatWeatherState(WeatherState weatherState) {
        String weatherName = weatherState.name().toLowerCase();
        return Character.toUpperCase(weatherName.charAt(0)) + weatherName.substring(1);
    }

    private void setPlannerControlsEnabled(boolean enabled) {
        museumVisitCheckBox.setEnabled(enabled);
        shoppingMallVisitCheckBox.setEnabled(enabled);
        parkVisitCheckBox.setEnabled(enabled);
        cityCenterVisitCheckBox.setEnabled(enabled);
        refreshActivityControlStyle(museumVisitCheckBox, enabled);
        refreshActivityControlStyle(shoppingMallVisitCheckBox, enabled);
        refreshActivityControlStyle(parkVisitCheckBox, enabled);
        refreshActivityControlStyle(cityCenterVisitCheckBox, enabled);
    }

    private void refreshActivityControlStyle(JCheckBox checkBox, boolean enabled) {
        checkBox.setBackground(enabled ? UiStyles.PANEL_BACKGROUND : UiStyles.SURFACE_MUTED);
        checkBox.setForeground(enabled ? UiStyles.TEXT_PRIMARY : UiStyles.TEXT_SECONDARY);
    }

    private PlannableCity buildPlanForSelectedCity(City selectedCity) {
        PlannableCity plan = new BaseCityPlan(selectedCity);

        if (museumVisitCheckBox.isSelected()) {
            plan = new MuseumVisitDecorator(plan);
        }

        if (shoppingMallVisitCheckBox.isSelected()) {
            plan = new ShoppingMallVisitDecorator(plan);
        }

        if (parkVisitCheckBox.isSelected()) {
            plan = new ParkVisitDecorator(plan);
        }

        if (cityCenterVisitCheckBox.isSelected()) {
            plan = new CityCenterVisitDecorator(plan);
        }

        return plan;
    }

    @Override
    public void updateWeatherData() {
        SwingUtilities.invokeLater(() -> {
            if (isDisplayable()) {
                refreshAllLists();
            }
        });
    }

    private void updateResponsiveLayout() {
        int availableWidth = getContentPane() != null ? getContentPane().getWidth() : getWidth();
        boolean shouldStack = availableWidth > 0 && availableWidth < STACKED_LAYOUT_BREAKPOINT;

        if (responsiveLayoutInitialized && stackedLayout == shouldStack) {
            return;
        }

        responsiveLayoutInitialized = true;
        stackedLayout = shouldStack;

        configureResponsiveSplitPane(listsSplitPane, shouldStack, LISTS_SPLIT_WEIGHT, 310, 460);
        configureResponsiveSplitPane(chartsSplitPane, shouldStack, CHARTS_SPLIT_WEIGHT, 340, 500);
        layoutPlannerCenter(shouldStack);
        revalidate();
        repaint();
    }

    private void configureResponsiveSplitPane(
            JSplitPane splitPane,
            boolean shouldStack,
            double resizeWeight,
            int widePreferredHeight,
            int stackedPreferredHeight
    ) {
        if (splitPane == null) {
            return;
        }

        int orientation = shouldStack ? JSplitPane.VERTICAL_SPLIT : JSplitPane.HORIZONTAL_SPLIT;
        splitPane.setOrientation(orientation);
        splitPane.setResizeWeight(resizeWeight);
        splitPane.setPreferredSize(new Dimension(
                0,
                shouldStack ? stackedPreferredHeight : widePreferredHeight
        ));
        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(resizeWeight));
    }

    private void shutdown() {
        weatherReportProvider.detach(this);
        weatherReportProvider.detach(temperatureChartPanel);
        weatherReportProvider.detach(weatherPieChartPanel);
        weatherReportProvider.stop();
        dispose();
    }
}
