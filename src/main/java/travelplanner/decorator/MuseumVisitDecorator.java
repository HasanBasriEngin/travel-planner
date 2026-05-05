package travelplanner.decorator;

/**
 * Adds a museum visit to the city plan.
 */
public class MuseumVisitDecorator extends ActivityDecorator {

    private static final double COST = 300.0;
    private static final double HOURS = 2.0;

    public MuseumVisitDecorator(PlannableCity wrappedPlan) {
        super(wrappedPlan);
    }

    @Override
    public String getDescription() {
        return wrappedPlan.getDescription() + ", Museum Visit";
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
