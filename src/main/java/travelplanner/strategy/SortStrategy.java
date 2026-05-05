package travelplanner.strategy;

import java.util.List;
import travelplanner.model.City;

/**
 * Defines a sorting strategy for city lists.
 */
public interface SortStrategy {

    List<City> sort(List<City> cities);
}
