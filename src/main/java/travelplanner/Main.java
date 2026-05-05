package travelplanner;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import travelplanner.view.MainFrame;

/**
 * Application entry point for the Travel Planner System.
 */
public final class Main {

    private Main() {
        // Prevent instantiation of the utility class.
    }

    public static void main(String[] args) {
        installLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    private static void installLookAndFeel() {
        try {
            FlatLightLaf.setup();
        } catch (RuntimeException exception) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignoredException) {
                // Keep Swing default look and feel as the last fallback.
            }
        }

        UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
    }
}
