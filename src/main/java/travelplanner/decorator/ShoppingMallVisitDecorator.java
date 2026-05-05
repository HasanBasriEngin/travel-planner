package travelplanner.decorator;

/**
 * Adds a shopping mall visit to the city plan.
 */
public class ShoppingMallVisitDecorator extends ActivityDecorator {

    private static final double COST = 500.0;
    private static final double HOURS = 3.0;

    public ShoppingMallVisitDecorator(PlannableCity wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription() + ", Shopping Mall Visit";
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
