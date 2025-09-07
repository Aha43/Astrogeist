package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import astrogeist.ui.swing.tool.abstraction.ColorLut;

/**
 * <p>
 *   A LUT-based color picker: choose a LUT preset and an intensity (0–100).
 *   Emits live color changes via Listener.
 * </p>
 */
public final class RampColorPicker extends JPanel {
    private static final long serialVersionUID = 1L;

    public interface Listener { void colorChanged(Color c); }

    private final JComboBox<String> lutCombo;
    private final JSlider slider = new JSlider(0, 100, 80);
    private final JPanel swatch = new JPanel();

    private final Map<String, ColorLut> lutMap;
    private ColorLut currentLut;
    private Listener listener;

    public RampColorPicker() {
        this(Luts.presets());
    }

    public RampColorPicker(Map<String, ColorLut> lutMap) {
        this.lutMap = lutMap;
        setLayout(new BorderLayout(8, 8));

        // Top: LUT selector
        lutCombo = new JComboBox<>(lutMap.keySet().toArray(new String[0]));
        lutCombo.setSelectedIndex(0);
        currentLut = lutMap.get(lutCombo.getSelectedItem());

        var top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        top.add(new JLabel("Palette:"));
        top.add(lutCombo);
        add(top, BorderLayout.NORTH);

        // Center: intensity slider
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        add(slider, BorderLayout.CENTER);

        // East: swatch preview
        swatch.setPreferredSize(new Dimension(40, 40));
        swatch.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        add(swatch, BorderLayout.EAST);

        // Wiring
        lutCombo.addActionListener(e -> {
            currentLut = lutMap.get(lutCombo.getSelectedItem());
            fireUpdate();
        });
        ChangeListener update = e -> fireUpdate();
        slider.addChangeListener(update);

        fireUpdate();
    }

    public void setListener(Listener l) { this.listener = l; }

    /** 0..100 slider value */
    public void setValue(int percent) {
        slider.setValue(Math.max(0, Math.min(100, percent)));
        fireUpdate();
    }

    public int getValue() { return slider.getValue(); }

    public Color getCurrentColor() {
        double t = slider.getValue() / 100.0;
        return currentLut.map(t);
    }

    public String getSelectedLutName() {
        return (String) lutCombo.getSelectedItem();
    }

    public void setSelectedLut(String name) {
        if (lutMap.containsKey(name)) {
            lutCombo.setSelectedItem(name);
        }
    }

    private void fireUpdate() {
        Color c = getCurrentColor();
        swatch.setBackground(c);
        if (listener != null) listener.colorChanged(c);
        revalidate();
        repaint();
    }
    
}
