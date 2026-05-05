package travelplanner.decorator;

/**
 * Adds a park visit to the city plan.
 */
public class ParkVisitDecorator extends ActivityDecorator {

    private static final double COST = 100.0;
    private static final double HOURS = 1.5;

    public ParkVisitDecorator(PlannableCity wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription() + ", Park Visit";
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
