package aha.common.ui.swing.panels;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import aha.common.util.MemoryUtil;

import javax.swing.Timer;

import static java.lang.System.gc;

/**
 * <p>
 *   Panel for inspecting memory use.
 * </p>
 */
public final class MemoryInspectorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JLabel usedLabel = new JLabel();
    private final JLabel maxLabel = new JLabel();

    public MemoryInspectorPanel() {
        setLayout(new GridLayout(0, 1));
        add(usedLabel);
        add(maxLabel);

        var gcButton = new JButton("Run GC");
        add(gcButton);

        gcButton.addActionListener(e -> { gc(); updateLabels(); });
        
        updateLabels();
        
        new Timer(2000, e -> updateLabels()).start();
    }

    private void updateLabels() {
        usedLabel.setText("Used heap: " + 
        	MemoryUtil.formatMB(MemoryUtil.usedHeapMB()));
        maxLabel.setText("Max heap: " +
        	MemoryUtil.formatMB(MemoryUtil.maxHeapMB()));
    }
}

