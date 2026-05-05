package travelplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import travelplanner.model.City;
import travelplanner.model.WeatherState;

/**
 * Renders city entries in a clearer two-line format for JList components.
 */
public class CityListCellRenderer extends JPanel implements ListCellRenderer<City> {

    private final JLabel titleLabel;
    private final JLabel detailsLabel;

    public CityListCellRenderer() {
        this.titleLabel = new JLabel();
        this.detailsLabel = new JLabel();

        setLayout(new BorderLayout(0, 6));
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        titleLabel.setFont(UiStyles.emphasisFont());
        titleLabel.setForeground(UiStyles.TEXT_PRIMARY);

        detailsLabel.setFont(UiStyles.smallFont());
        detailsLabel.setForeground(UiStyles.TEXT_SECONDARY);

        add(titleLabel, BorderLayout.NORTH);
        add(detailsLabel, BorderLayout.CENTER);
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
            return this;
        }

        titleLabel.setText(city.getName() + "  |  " + formatWeatherState(city.getCurrentWeatherState()));
        detailsLabel.setText(String.format(
                "Pop: %,d   |   Area: %.0f km2   |   Temp: %.1f C",
                city.getPopulation(),
                city.getArea(),
                city.getCurrentTemperature()
        ));

        Color background = isSelected ? UiStyles.ACCENT_SOFT : UiStyles.CARD_BACKGROUND;
        Color borderColor = isSelected ? UiStyles.ACCENT : UiStyles.CARD_BORDER;
        Color titleColor = isSelected ? UiStyles.ACCENT : UiStyles.TEXT_PRIMARY;

        setBackground(background);
        titleLabel.setForeground(titleColor);
        detailsLabel.setForeground(UiStyles.TEXT_SECONDARY);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, isSelected ? 2 : 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        return this;
    }

    private String formatWeatherState(WeatherState weatherState) {
        String weatherName = weatherState.name().toLowerCase();
        return Character.toUpperCase(weatherName.charAt(0)) + weatherName.substring(1);
    }
}
