package travelplanner.observer;

/**
 * Receives weather update notifications from a weather subject.
 */
public interface WeatherObserver {

    void updateWeatherData();
}
