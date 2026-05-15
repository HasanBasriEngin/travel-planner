package travelplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import travelplanner.model.City;
import travelplanner.model.WeatherState;

/**
 * Renders city entries in a clearer two-line format for JList components.
 */
public class CityListCellRenderer extends JPanel implements ListCellRenderer<City> {

    private final JLabel titleLabel;
    private final JLabel detailsLabel;
    private final JLabel weatherLabel;

    public CityListCellRenderer() {
        this.titleLabel = new JLabel();
        this.detailsLabel = new JLabel();
        this.weatherLabel = new WeatherBadge();

        JPanel textPanel = new JPanel(new BorderLayout(0, 6));
        textPanel.setOpaque(false);

        setLayout(new BorderLayout(12, 0));
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(11, 16, 11, 16));

        titleLabel.setFont(UiStyles.emphasisFont());
        titleLabel.setForeground(UiStyles.TEXT_PRIMARY);

        detailsLabel.setFont(UiStyles.smallFont());
        detailsLabel.setForeground(UiStyles.TEXT_SECONDARY);

        weatherLabel.setFont(UiStyles.smallEmphasisFont());
        weatherLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weatherLabel.setOpaque(false);
        weatherLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(detailsLabel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.CENTER);
        add(weatherLabel, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends City> list,
            City city,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        if (city == null) {
            titleLabel.setText("");
            detailsLabel.setText("");
            weatherLabel.setText("");
            return this;
        }

        String weatherText = formatWeatherState(city.getCurrentWeatherState());
        titleLabel.setText(city.getName());
        detailsLabel.setText(String.format(
                "Population %,d    %.0f km2    %.1f C",
                city.getPopulation(),
                city.getArea(),
                city.getCurrentTemperature()
        ));
        weatherLabel.setText(weatherText);

        Color background = isSelected ? UiStyles.SURFACE_TINT : UiStyles.CARD_BACKGROUND;
        Color titleColor = isSelected ? UiStyles.ACCENT_DARK : UiStyles.TEXT_PRIMARY;
        Color weatherColor = getWeatherColor(city.getCurrentWeatherState());

        setBackground(background);
        titleLabel.setForeground(titleColor);
        detailsLabel.setForeground(UiStyles.TEXT_SECONDARY);
        weatherLabel.setBackground(getWeatherBackgroundColor(city.getCurrentWeatherState()));
        weatherLabel.setForeground(weatherColor);

        if (isSelected) {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 1, 0, UiStyles.ACCENT),
                    BorderFactory.createEmptyBorder(11, 12, 11, 16)
            ));
        } else {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, UiStyles.CARD_BORDER),
                    BorderFactory.createEmptyBorder(11, 16, 11, 16)
            ));
        }

        return this;
    }

    private Color getWeatherColor(WeatherState weatherState) {
        if (weatherState == WeatherState.SUNNY) {
            return UiStyles.SUNNY_COLOR;
        }

        if (weatherState == WeatherState.CLOUDY) {
            return UiStyles.CLOUDY_COLOR;
        }

        if (weatherState == WeatherState.RAINY) {
            return UiStyles.RAINY_COLOR;
        }

        return UiStyles.SNOWY_COLOR;
    }

    private Color getWeatherBackgroundColor(WeatherState weatherState) {
        if (weatherState == WeatherState.SUNNY) {
            return new Color(255, 247, 237);
        }

        if (weatherState == WeatherState.CLOUDY) {
            return new Color(241, 245, 249);
        }

        if (weatherState == WeatherState.RAINY) {
            return new Color(240, 253, 250);
        }

        return new Color(240, 249, 255);
    }

    private String formatWeatherState(WeatherState weatherState) {
        String weatherName = weatherState.name().toLowerCase();
        return Character.toUpperCase(weatherName.charAt(0)) + weatherName.substring(1);
    }

    private static final class WeatherBadge extends JLabel {

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(getBackground());
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }
}
