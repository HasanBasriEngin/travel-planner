package travelplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

    public TemperatureChartPanel() {
        this.cityRepository = CityRepository.getInstance();
        this.dataset = new DefaultCategoryDataset();
        this.chartPanel = createChartPanel();

        setLayout(new BorderLayout(0, 14));
        setOpaque(true);
        setBackground(UiStyles.CARD_BACKGROUND);
        setBorder(UiStyles.createCardBorder());
        setMinimumSize(new Dimension(320, 300));
        setPreferredSize(new Dimension(420, 340));
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
        CategoryAxis domainAxis = plot.getDomainAxis();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        chart.setBackgroundPaint(UiStyles.CARD_BACKGROUND);
        chart.removeLegend();

        plot.setBackgroundPaint(UiStyles.SURFACE_MUTED);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(216, 223, 230));
        plot.setDomainGridlinesVisible(false);

        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 9.0));
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
        panel.setMouseWheelEnabled(true);
        panel.setMinimumDrawWidth(240);
        panel.setMinimumDrawHeight(200);
        panel.setPreferredSize(new Dimension(380, 260));
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
