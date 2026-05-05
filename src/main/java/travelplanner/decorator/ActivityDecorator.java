package travelplanner.decorator;

/**
 * Base decorator for adding activities to a city plan.
 */
public abstract class ActivityDecorator implements PlannableCity {

    protected final PlannableCity wrappedPlan;

    protected ActivityDecorator(PlannableCity wrappedPlan) {
        this.wrappedPlan = wrappedPlan;
    }
}
