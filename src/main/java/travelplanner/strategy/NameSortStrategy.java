package travelplanner.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import travelplanner.model.City;

/**
 * Sorts cities alphabetically by name.
 */
public class NameSortStrategy implements SortStrategy {

    @Override
    public List<City> sort(List<City> cities) {
        List<City> sortedCities = new ArrayList<>(cities);
        sortedCities.sort(Comparator.comparing(City::getName, String.CASE_INSENSITIVE_ORDER));
        return sortedCities;
    }
}
