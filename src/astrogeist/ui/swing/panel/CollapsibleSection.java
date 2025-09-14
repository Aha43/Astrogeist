package astrogeist.ui.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

/**
 * <p>
 *   A simple collapsible panel with a clickable header.
 *   Uses a JToggleButton (▸/▾) to show/hide the content panel.
 * </p>
 */
public final class CollapsibleSection extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JToggleButton header;
    private final JPanel content;

    public CollapsibleSection(String title, JComponent contentComponent, boolean initiallyExpanded) {
        super(new BorderLayout());
        // Header with arrow + title
        header = new JToggleButton();
        header.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        header.setHorizontalAlignment(SwingConstants.LEFT);
        header.setFocusPainted(false);
        header.setContentAreaFilled(false);
        header.setSelected(initiallyExpanded);
        updateHeaderText(title);

        // Content wrapper (so we can hide/show cleanly)
        content = new JPanel(new BorderLayout());
        content.add(contentComponent, BorderLayout.CENTER);
        content.setVisible(initiallyExpanded);

        // Divider line below header (optional)
        var headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(header, BorderLayout.CENTER);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        add(headerPanel, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        header.addActionListener(e -> {
            content.setVisible(header.isSelected());
            updateHeaderText(title);
            revalidate();
            repaint();
        });
    }

    private final void updateHeaderText(String title) {
        // ▾ expanded, ▸ collapsed
        String arrow = header.isSelected() ? "▾" : "▸";
        header.setText(" " + arrow + "  " + title);
    }

    public final boolean isExpanded() { return header.isSelected(); }
    
    public final void setExpanded(boolean expanded) {
        header.setSelected(expanded);
        content.setVisible(expanded);
        updateHeaderText(header.getText());
        revalidate();
        repaint();
    }
    
}

