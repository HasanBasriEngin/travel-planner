package travelplanner.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import travelplanner.model.City;

/**
 * Sorts cities by area in ascending order.
 */
public class AreaSortStrategy implements SortStrategy {

    @Override
    public List<City> sort(List<City> cities) {
        List<City> sortedCities = new ArrayList<>(cities);
        sortedCities.sort(Comparator.comparingDouble(City::getArea));
        return sortedCities;
    }
}
