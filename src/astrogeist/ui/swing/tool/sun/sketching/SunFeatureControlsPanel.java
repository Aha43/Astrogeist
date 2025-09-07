package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public final class SunFeatureControlsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public SunFeatureControlsPanel(SunPanel sun) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 6));

        // Sunspot tool toggle
        JToggleButton addSpot = new JToggleButton("● Sunspot");
        addSpot.setToolTipText("Add Sunspot: click on the disk to place");
        addSpot.addActionListener(e -> {
            boolean on = addSpot.isSelected();
            sun.setTool(on ? SunPanel.Tool.ADD_SUNSPOT : SunPanel.Tool.NONE);
        });

        // Ensure tool resets if the panel gets disabled (safety)
        addSpot.addPropertyChangeListener("enabled", evt -> {
            if (Boolean.FALSE.equals(evt.getNewValue())) {
                addSpot.setSelected(false);
                sun.setTool(SunPanel.Tool.NONE);
            }
        });

        // Optional: clear button for quick testing
        JButton clear = new JButton("Clear Spots");
        clear.addActionListener(e -> sun.clearSunspots());

        add(new JLabel("Features:"));
        add(addSpot);
        add(Box.createHorizontalStrut(12));
        add(clear);
    }
}

