package travelplanner.decorator;

import travelplanner.model.City;

/**
 * Represents the base plan for a selected city.
 */
public class BaseCityPlan implements PlannableCity {

    private final City city;

    public BaseCityPlan(City city) {
        this.city = city;
    }

    @Override
    public String getDescription() {
        return city.getName();
    }

    @Override
    public double getTotalCost() {
        return 0.0;
    }

    @Override
    public double getTotalHours() {
        return 0.0;
    }
}
