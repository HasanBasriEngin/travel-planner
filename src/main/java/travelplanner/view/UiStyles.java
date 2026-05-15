package travelplanner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Centralizes lightweight Swing styling used by the Travel Planner UI.
 */
public final class UiStyles {

    public static final Color APP_BACKGROUND = new Color(232, 244, 255);
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color CARD_BORDER = new Color(224, 231, 239);
    public static final Color SURFACE_MUTED = new Color(241, 248, 255);
    public static final Color SURFACE_TINT = new Color(232, 247, 245);
    public static final Color CONTROL_BACKGROUND = new Color(236, 247, 255);
    public static final Color PANEL_BACKGROUND = new Color(252, 254, 255);
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color ACCENT = new Color(15, 118, 110);
    public static final Color ACCENT_DARK = new Color(17, 94, 89);
    public static final Color ACCENT_SOFT = new Color(204, 251, 241);
    public static final Color SECONDARY_ACCENT = new Color(79, 70, 229);
    public static final Color WARM_ACCENT = new Color(234, 88, 12);
    public static final Color SUNNY_COLOR = new Color(245, 158, 11);
    public static final Color CLOUDY_COLOR = new Color(100, 116, 139);
    public static final Color RAINY_COLOR = new Color(20, 184, 166);
    public static final Color SNOWY_COLOR = new Color(14, 165, 233);

    private static final String UI_FONT_NAME = "Segoe UI";

    private UiStyles() {
        // Utility class
    }

    public static Font headingFont() {
        return new Font(UI_FONT_NAME, Font.BOLD, 26);
    }

    public static Font sectionTitleFont() {
        return new Font(UI_FONT_NAME, Font.BOLD, 17);
    }

    public static Font bodyFont() {
        return new Font(UI_FONT_NAME, Font.PLAIN, 14);
    }

    public static Font smallFont() {
        return new Font(UI_FONT_NAME, Font.PLAIN, 12);
    }

    public static Font smallEmphasisFont() {
        return new Font(UI_FONT_NAME, Font.BOLD, 12);
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
                BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
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
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBackground(CONTROL_BACKGROUND);
        comboBox.setPreferredSize(new Dimension(170, 36));
        comboBox.setMinimumSize(new Dimension(170, 36));
        comboBox.setMaximumSize(new Dimension(170, 36));
        comboBox.setFocusable(false);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );
                label.setFont(bodyFont());
                label.setForeground(TEXT_PRIMARY);
                label.setBackground(isSelected ? ACCENT_SOFT : CONTROL_BACKGROUND);
                label.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                return label;
            }
        });
        comboBox.putClientProperty(
                "FlatLaf.style",
                "arc: 10; borderWidth: 1; focusWidth: 1; background: #ECF7FF; buttonBackground: #ECF7FF"
        );
    }

    public static void styleCheckBox(JCheckBox checkBox) {
        checkBox.setFont(bodyFont());
        checkBox.setBackground(PANEL_BACKGROUND);
        checkBox.setForeground(TEXT_PRIMARY);
        checkBox.setFocusPainted(false);
        checkBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        checkBox.setBorderPainted(true);
        checkBox.putClientProperty("FlatLaf.style", "arc: 12; icon.focusWidth: 0");
    }

    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER));
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
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, CARD_BORDER),
                BorderFactory.createEmptyBorder(12, 0, 12, 0)
        ));
        JLabel titleLabel = createMutedLabel(title);
        titleLabel.setFont(smallEmphasisFont());
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }
}
