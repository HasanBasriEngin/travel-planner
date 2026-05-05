package travelplanner.view;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import travelplanner.model.City;

/**
 * Keeps city lists aligned to the viewport width to avoid horizontal overflow.
 */
public class ResponsiveCityList extends JList<City> {

    public ResponsiveCityList(DefaultListModel<City> listModel) {
        super(listModel);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }
}
