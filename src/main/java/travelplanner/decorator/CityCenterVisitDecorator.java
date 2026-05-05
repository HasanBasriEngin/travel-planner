package travelplanner.decorator;

/**
 * Adds a city center visit to the city plan.
 */
public class CityCenterVisitDecorator extends ActivityDecorator {

    private static final double COST = 250.0;
    private static final double HOURS = 2.5;

    public CityCenterVisitDecorator(PlannableCity wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription() + ", City Center Visit";
    }

    @Override
    public double getTotalCost() {
        return wrappedPlan.getTotalCost() + COST;
    }

    @Override
    public double getTotalHours() {
        return wrappedPlan.getTotalHours() + HOURS;
    }
}
