package travelplanner.iterator;

import travelplanner.model.City;

/**
 * Defines the operations for custom city iterators.
 */
public interface CityIterator {

    boolean hasNext();

    City next();
}
