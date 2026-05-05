package travelplanner.view;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * Makes a panel fill the viewport width while still allowing vertical scrolling.
 */
public class ViewportWidthPanel extends JPanel implements Scrollable {

    public ViewportWidthPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 24;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            return Math.max(visibleRect.height - 48, 48);
        }

        return Math.max(visibleRect.width - 48, 48);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
