package travelplanner.iterator;

import java.util.ArrayList;
import java.util.List;
import travelplanner.model.City;

/**
 * Provides custom iterators for city lists filtered by weather state.
 */
public class WeatherFilteredCollection {

    private final List<City> cities;

    public WeatherFilteredCollection(List<City> cities) {
        this.cities = new ArrayList<>(cities);
    }

    public CityIterator createSunnyIterator() {
        return new SunnyCityIterator(cities);
    }

    public CityIterator createCloudyIterator() {
        return new CloudyCityIterator(cities);
    }

    public CityIterator createRainyIterator() {
        return new RainyCityIterator(cities);
    }

    public CityIterator createSnowyIterator() {
        return new SnowyCityIterator(cities);
    }
}
