package travelplanner.observer;

import java.util.ArrayList;
import java.util.List;
import travelplanner.repository.CityRepository;

/**
 * Periodically updates city weather data and notifies observers.
 */
public class WeatherReportProvider implements WeatherSubject, Runnable {

    private static final long UPDATE_INTERVAL_MILLISECONDS = 3_000L;

    private final List<WeatherObserver> observers;
    private final CityRepository cityRepository;

    private volatile boolean running;
    private Thread workerThread;

    public WeatherReportProvider() {
        this.observers = new ArrayList<>();
        this.cityRepository = CityRepository.getInstance();
    }

    @Override
    public synchronized void attach(WeatherObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public synchronized void detach(WeatherObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        List<WeatherObserver> observerSnapshot;

        synchronized (this) {
            observerSnapshot = new ArrayList<>(observers);
        }

        for (WeatherObserver observer : observerSnapshot) {
            observer.updateWeatherData();
        }
    }

    public synchronized void start() {
        if (workerThread != null && workerThread.isAlive()) {
            return;
        }

        running = true;
        workerThread = new Thread(this, "WeatherReportProvider");
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public synchronized void stop() {
        running = false;

        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(UPDATE_INTERVAL_MILLISECONDS);

                if (!running) {
                    break;
                }

                cityRepository.updateRandomCityWeather();
                notifyObservers();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        } finally {
            synchronized (this) {
                workerThread = null;
                running = false;
            }
        }
    }
}
