package travelplanner.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import travelplanner.model.City;
import travelplanner.model.WeatherState;

/**
 * Iterates over cities with rainy weather.
 */
public class RainyCityIterator implements CityIterator {

    private final List<City> cities;
    private int currentIndex;

    public RainyCityIterator(List<City> cities) {
        this.cities = new ArrayList<>(cities);
        this.currentIndex = findNextIndex(0);
    }

    @Override
    public boolean hasNext() {
        return currentIndex < cities.size();
    }

    @Override
    public City next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more rainy cities are available.");
        }

        City city = cities.get(currentIndex);
        currentIndex = findNextIndex(currentIndex + 1);
        return city;
    }

    private int findNextIndex(int startIndex) {
        int index = startIndex;

        while (index < cities.size()) {
            if (cities.get(index).getCurrentWeatherState() == WeatherState.RAINY) {
                return index;
            }
            index++;
        }

        return cities.size();
    }
}
