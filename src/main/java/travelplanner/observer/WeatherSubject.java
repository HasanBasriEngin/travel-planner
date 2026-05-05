package travelplanner.observer;

/**
 * Defines the subject operations for weather data notifications.
 */
public interface WeatherSubject {

    void attach(WeatherObserver observer);

    void detach(WeatherObserver observer);

    void notifyObservers();
}
