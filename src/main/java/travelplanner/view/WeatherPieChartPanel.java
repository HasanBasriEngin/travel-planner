package travelplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ui.RectangleEdge;
import travelplanner.model.WeatherState;
import travelplanner.observer.WeatherObserver;
import travelplanner.repository.CityRepository;

/**
 * Displays the distribution of weather states as a pie chart.
 */
public class WeatherPieChartPanel extends JPanel implements WeatherObserver {

    private final CityRepository cityRepository;
    private final DefaultPieDataset<String> dataset;
    private final ChartPanel chartPanel;

    public WeatherPieChartPanel() {
        this.cityRepository = CityRepository.getInstance();
        this.dataset = new DefaultPieDataset<>();
        this.chartPanel = createChartPanel();

        setLayout(new BorderLayout(0, 14));
        setOpaque(true);
        setBackground(UiStyles.CARD_BACKGROUND);
        setBorder(UiStyles.createCardBorder());
        setMinimumSize(new Dimension(320, 300));
        setPreferredSize(new Dimension(400, 340));
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        refreshChartData();
    }

    private ChartPanel createChartPanel() {
        JFreeChart chart = ChartFactory.createPieChart(
                null,
                dataset,
                true,
                true,
                false
        );

        PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
        chart.setBackgroundPaint(UiStyles.CARD_BACKGROUND);

        plot.setBackgroundPaint(UiStyles.CARD_BACKGROUND);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setLabelBackgroundPaint(Color.WHITE);
        plot.setLabelOutlinePaint(UiStyles.CARD_BORDER);
        plot.setLabelShadowPaint(null);
        plot.setLabelFont(UiStyles.smallFont());
        plot.setLabelPaint(UiStyles.TEXT_PRIMARY);
        plot.setLabelLinkPaint(UiStyles.TEXT_SECONDARY);
        plot.setSectionOutlinesVisible(false);
        plot.setSimpleLabels(true);
        plot.setInteriorGap(0.04);
        plot.setCircular(true);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {1}",
                NumberFormat.getIntegerInstance(),
                NumberFormat.getPercentInstance()
        ));

        if (chart.getLegend() != null) {
            chart.getLegend().setPosition(RectangleEdge.BOTTOM);
            chart.getLegend().setBackgroundPaint(UiStyles.CARD_BACKGROUND);
            chart.getLegend().setItemFont(UiStyles.bodyFont());
        }

        ChartPanel panel = new ChartPanel(chart);
        panel.setBorder(BorderFactory.createEmptyBorder());
        panel.setOpaque(false);
        panel.setBackground(UiStyles.CARD_BACKGROUND);
        panel.setMouseWheelEnabled(true);
        panel.setMinimumDrawWidth(240);
        panel.setMinimumDrawHeight(240);
        panel.setPreferredSize(new Dimension(340, 260));
        return panel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 4));
        headerPanel.setOpaque(false);

        JLabel titleLabel = UiStyles.createSectionTitle("Weather Distribution");
        JLabel subtitleLabel = UiStyles.createMutedLabel("Current weather mix across the tracked cities.");

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private void refreshChartData() {
        dataset.clear();

        Map<WeatherState, Integer> weatherCounts = cityRepository.getWeatherCounts();
        for (WeatherState weatherState : WeatherState.values()) {
            dataset.setValue(weatherState.name(), weatherCounts.get(weatherState));
        }

        PiePlot<?> plot = (PiePlot<?>) chartPanel.getChart().getPlot();
        plot.setSectionPaint(WeatherState.SUNNY.name(), UiStyles.SUNNY_COLOR);
        plot.setSectionPaint(WeatherState.CLOUDY.name(), UiStyles.CLOUDY_COLOR);
        plot.setSectionPaint(WeatherState.RAINY.name(), UiStyles.RAINY_COLOR);
        plot.setSectionPaint(WeatherState.SNOWY.name(), UiStyles.SNOWY_COLOR);
    }

    @Override
    public void updateWeatherData() {
        SwingUtilities.invokeLater(() -> {
            if (isDisplayable()) {
                refreshChartData();
                chartPanel.repaint();
            }
        });
    }
}
