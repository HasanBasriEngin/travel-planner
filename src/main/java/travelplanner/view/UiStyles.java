package travelplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Centralizes lightweight Swing styling used by the Travel Planner UI.
 */
public final class UiStyles {

    public static final Color APP_BACKGROUND = new Color(243, 246, 250);
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color CARD_BORDER = new Color(220, 226, 233);
    public static final Color SURFACE_MUTED = new Color(247, 249, 252);
    public static final Color TEXT_PRIMARY = new Color(31, 41, 55);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color ACCENT = new Color(42, 110, 240);
    public static final Color ACCENT_SOFT = new Color(231, 239, 255);
    public static final Color SUNNY_COLOR = new Color(242, 178, 64);
    public static final Color CLOUDY_COLOR = new Color(120, 144, 174);
    public static final Color RAINY_COLOR = new Color(64, 149, 120);
    public static final Color SNOWY_COLOR = new Color(120, 197, 235);

    private static final String UI_FONT_NAME = "Segoe UI";

    private UiStyles() {
        // Utility class
    }

    public static Font headingFont() {
        return new Font(UI_FONT_NAME, Font.BOLD, 24);
    }

    public static Font sectionTitleFont() {
        return new Font(UI_FONT_NAME, Font.BOLD, 18);
    }

    public static Font bodyFont() {
        return new Font(UI_FONT_NAME, Font.PLAIN, 14);
    }

    public static Font smallFont() {
        return new Font(UI_FONT_NAME, Font.PLAIN, 12);
    }

    public static Font emphasisFont() {
        return new Font(UI_FONT_NAME, Font.BOLD, 16);
    }

    public static JPanel createCardPanel() {
        JPanel cardPanel = new JPanel(new BorderLayout(0, 14));
        cardPanel.setOpaque(true);
        cardPanel.setBackground(CARD_BACKGROUND);
        cardPanel.setBorder(createCardBorder());
        return cardPanel;
    }

    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
    }

    public static JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(sectionTitleFont());
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createMutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(bodyFont());
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(bodyFont());
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(emphasisFont());
        label.setForeground(TEXT_PRIMARY);
        label.setVerticalAlignment(SwingConstants.TOP);
        return label;
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(bodyFont());
    }

    public static void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(bodyFont());
        checkBox.setBackground(CARD_BACKGROUND);
        checkBox.setForeground(TEXT_PRIMARY);
        checkBox.setFocusPainted(false);
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(CARD_BORDER));
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);
    }

    public static void styleTextArea(JTextArea textArea) {
        textArea.setFont(bodyFont());
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setBackground(SURFACE_MUTED);
        textArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
    }

    public static JPanel createInfoPanel(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(true);
        panel.setBackground(SURFACE_MUTED);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        JLabel titleLabel = createMutedLabel(title);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }
}
