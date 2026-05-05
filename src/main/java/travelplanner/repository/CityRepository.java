package travelplanner.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import travelplanner.model.City;
import travelplanner.model.WeatherState;

/**
 * Stores and manages city data for the application.
 */
public final class CityRepository {

    private static final String CITY_RESOURCE = "cities.json";
    private static final WeatherState[] WEATHER_STATES = WeatherState.values();
    private static final CityRepository INSTANCE = new CityRepository();

    private final List<City> cities;
    private final Random random;

    private CityRepository() {
        this.random = new Random();
        this.cities = loadCities();
    }

    public static CityRepository getInstance() {
        return INSTANCE;
    }

    public synchronized List<City> getCities() {
        List<City> cityCopies = new ArrayList<>();
        for (City city : cities) {
            cityCopies.add(new City(city));
        }
        return Collections.unmodifiableList(cityCopies);
    }

    public synchronized void updateRandomCityWeather() {
        if (cities.isEmpty()) {
            return;
        }

        List<City> citiesToUpdate = new ArrayList<>(cities);
        Collections.shuffle(citiesToUpdate, random);

        int updateCount = 1 + random.nextInt(Math.min(3, cities.size()));
        for (int index = 0; index < updateCount; index++) {
            City city = citiesToUpdate.get(index);
            city.setCurrentTemperature(generateUpdatedTemperature(city.getCurrentTemperature()));
            city.setCurrentWeatherState(generateUpdatedWeatherState(city.getCurrentWeatherState()));
        }
    }

    public synchronized Map<WeatherState, Integer> getWeatherCounts() {
        Map<WeatherState, Integer> weatherCounts = new EnumMap<>(WeatherState.class);

        for (WeatherState weatherState : WeatherState.values()) {
            weatherCounts.put(weatherState, 0);
        }

        for (City city : cities) {
            WeatherState weatherState = city.getCurrentWeatherState();
            weatherCounts.put(weatherState, weatherCounts.get(weatherState) + 1);
        }

        return Collections.unmodifiableMap(weatherCounts);
    }

    private List<City> loadCities() {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CITY_RESOURCE)) {
            if (inputStream == null) {
                throw new IllegalStateException("Could not find resource file: " + CITY_RESOURCE);
            }

            City[] loadedCities = objectMapper.readValue(inputStream, City[].class);
            return new ArrayList<>(Arrays.asList(loadedCities));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load city data from JSON.", exception);
        }
    }

    private double generateUpdatedTemperature(double currentTemperature) {
        double updatedTemperature = currentTemperature;

        while (Double.compare(updatedTemperature, currentTemperature) == 0) {
            double changeAmount = -5.0 + (10.0 * random.nextDouble());
            updatedTemperature = roundToOneDecimal(currentTemperature + changeAmount);
        }

        return updatedTemperature;
    }

    private WeatherState generateUpdatedWeatherState(WeatherState currentWeatherState) {
        WeatherState updatedWeatherState = currentWeatherState;

        while (updatedWeatherState == currentWeatherState) {
            updatedWeatherState = WEATHER_STATES[random.nextInt(WEATHER_STATES.length)];
        }

        return updatedWeatherState;
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
