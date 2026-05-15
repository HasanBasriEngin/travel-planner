package travelplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import travelplanner.model.City;
import travelplanner.observer.WeatherObserver;
import travelplanner.repository.CityRepository;

/**
 * Displays a bar chart of city temperatures.
 */
public class TemperatureChartPanel extends JPanel implements WeatherObserver {

    private static final String SERIES_KEY = "Temperature";

    private final CityRepository cityRepository;
    private final DefaultCategoryDataset dataset;
    private final ChartPanel chartPanel;
    private CategoryAxis domainAxis;
    private double currentLabelAngle;

    public TemperatureChartPanel() {
        this.cityRepository = CityRepository.getInstance();
        this.dataset = new DefaultCategoryDataset();
        this.chartPanel = createChartPanel();

        setLayout(new BorderLayout(0, 8));
        setOpaque(true);
        setBackground(UiStyles.CARD_BACKGROUND);
        setBorder(UiStyles.createCardBorder());
        setMinimumSize(new Dimension(320, 280));
        setPreferredSize(new Dimension(520, 340));
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        refreshChartData();
    }

    private ChartPanel createChartPanel() {
        JFreeChart chart = ChartFactory.createBarChart(
                null,
                "Cities",
                "Temperature (C)",
                dataset
        );

        CategoryPlot plot = chart.getCategoryPlot();
        domainAxis = plot.getDomainAxis();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        chart.setBackgroundPaint(UiStyles.CARD_BACKGROUND);
        chart.removeLegend();

        plot.setBackgroundPaint(UiStyles.CARD_BACKGROUND);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(226, 232, 240));
        plot.setDomainGridlinesVisible(false);

        currentLabelAngle = -1.0;
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 8.0));
        domainAxis.setLabelFont(UiStyles.bodyFont());
        domainAxis.setTickLabelFont(UiStyles.smallFont());
        domainAxis.setTickLabelPaint(UiStyles.TEXT_SECONDARY);
        domainAxis.setLabelPaint(UiStyles.TEXT_PRIMARY);

        rangeAxis.setLabelFont(UiStyles.bodyFont());
        rangeAxis.setTickLabelFont(UiStyles.smallFont());
        rangeAxis.setTickLabelPaint(UiStyles.TEXT_SECONDARY);
        rangeAxis.setLabelPaint(UiStyles.TEXT_PRIMARY);
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());

        renderer.setSeriesPaint(0, UiStyles.ACCENT);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(0.08);

        ChartPanel panel = new ChartPanel(chart);
        panel.setBorder(BorderFactory.createEmptyBorder());
        panel.setOpaque(false);
        panel.setBackground(UiStyles.CARD_BACKGROUND);
        panel.setMouseWheelEnabled(false);
        panel.setMouseZoomable(false);
        panel.setDomainZoomable(false);
        panel.setRangeZoomable(false);
        panel.setPopupMenu(null);
        panel.setMinimumDrawWidth(240);
        panel.setMinimumDrawHeight(220);
        panel.setPreferredSize(new Dimension(440, 260));
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                updateCategoryLabelRotation();
            }
        });
        return panel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 4));
        headerPanel.setOpaque(false);

        JLabel titleLabel = UiStyles.createSectionTitle("City Temperatures");
        JLabel subtitleLabel = UiStyles.createMutedLabel("Live temperature comparison for all cities.");

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private void refreshChartData() {
        dataset.clear();

        List<City> cities = cityRepository.getCities();
        for (City city : cities) {
            dataset.addValue(city.getCurrentTemperature(), SERIES_KEY, city.getName());
        }

        updateCategoryLabelRotation();
    }

    private void updateCategoryLabelRotation() {
        if (domainAxis == null) {
            return;
        }

        int categoryCount = Math.max(dataset.getColumnCount(), 1);
        int availableWidth = chartPanel != null && chartPanel.getWidth() > 0
                ? chartPanel.getWidth()
                : chartPanel.getPreferredSize().width;
        double pixelsPerCategory = (double) availableWidth / categoryCount;
        double newLabelAngle = getLabelAngleForCategoryWidth(pixelsPerCategory);

        if (Math.abs(newLabelAngle - currentLabelAngle) < 0.001) {
            return;
        }

        currentLabelAngle = newLabelAngle;
        if (Double.compare(newLabelAngle, 0.0) == 0) {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        } else {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(newLabelAngle));
        }
    }

    private double getLabelAngleForCategoryWidth(double pixelsPerCategory) {
        if (pixelsPerCategory >= 70.0) {
            return 0.0;
        }

        if (pixelsPerCategory >= 52.0) {
            return Math.PI / 12.0;
        }

        if (pixelsPerCategory >= 40.0) {
            return Math.PI / 8.0;
        }

        if (pixelsPerCategory >= 30.0) {
            return Math.PI / 5.0;
        }

        if (pixelsPerCategory >= 22.0) {
            return Math.PI / 3.0;
        }

        return Math.PI / 2.4;
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
