package travelplanner.decorator;

/**
 * Defines the planning information for a city activity plan.
 */
public interface PlannableCity {

    String getDescription();

    double getTotalCost();

    double getTotalHours();
}
