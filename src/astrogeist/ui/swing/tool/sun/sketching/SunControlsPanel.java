package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.*;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import javax.swing.*;

import org.w3c.dom.Document;

import astrogeist.ui.swing.component.general.CollapsibleSection;

public final class SunControlsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public SunControlsPanel(SunPanel sun, Path saveFolder) {
        setLayout(new BorderLayout());
        var root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JScrollPane(root,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        ), BorderLayout.CENTER);

        // --- Always-visible: border/padding slider ---------------------------
        var base = new JPanel(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        var padLabel = new JLabel("Border padding (%)");
        var padSlider = new JSlider(0, 45, (int) Math.round(sun.getPaddingFraction() * 100));
        padSlider.setMajorTickSpacing(5);
        padSlider.setPaintTicks(true);
        padSlider.setPaintLabels(true);
        padSlider.addChangeListener(e -> sun.setPaddingFraction(padSlider.getValue() / 100.0));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; base.add(padLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; base.add(padSlider, gbc);

        base.setBorder(BorderFactory.createTitledBorder("Layout"));
        root.add(base);
        root.add(Box.createVerticalStrut(6));

        // --- Collapsible: Background (greyscale) ------------------------------
        var bgPanel = new JPanel(new GridBagLayout());
        var gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(6, 6, 6, 6);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1;

        var bgPicker = new RampColorPicker();
        bgPicker.setSelectedLut("Greyscale"); // keep background neutral
        bgPicker.setListener(sun::setBackgroundColor);

        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.gridwidth = 1;
        bgPanel.add(new JLabel("Background"), gbc2);
        gbc2.gridx = 1; gbc2.gridy = 0; gbc2.gridwidth = 2;
        bgPanel.add(bgPicker, gbc2);

        var bgSection = new CollapsibleSection("Background Color", bgPanel, false);
        root.add(bgSection);
        root.add(Box.createVerticalStrut(6));

        // --- Collapsible: Disk & Limb (LUT + intensity) ----------------------
        var dlPanel = new JPanel(new GridBagLayout());
        var gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(6, 6, 6, 6);
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.weightx = 1;

        var dlPicker = new RampColorPicker(); // LUT dropdown: Greyscale, Classic Red, etc.

        var applyDisk = new JButton("Apply to Disk");
        applyDisk.addActionListener(e -> sun.setSunFill(dlPicker.getCurrentColor()));
        var applyLimb = new JButton("Apply to Limb");
        applyLimb.addActionListener(e -> sun.setLimbStroke(dlPicker.getCurrentColor()));

        gbc3.gridx = 0; gbc3.gridy = 0; gbc3.gridwidth = 1;
        dlPanel.add(new JLabel("Disk / Limb"), gbc3);
        gbc3.gridx = 1; gbc3.gridy = 0; gbc3.gridwidth = 2;
        dlPanel.add(dlPicker, gbc3);

        gbc3.gridx = 1; gbc3.gridy = 1; gbc3.gridwidth = 2;
        var actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        actions.add(applyDisk);
        actions.add(applyLimb);
        dlPanel.add(actions, gbc3);

        var dlSection = new CollapsibleSection("Disk & Limb Colors", dlPanel, true);
        root.add(dlSection);
    }
    
    void saveSketch(Path folder,
            SunPanel sun,
            RampColorPicker diskPicker,
            RampColorPicker limbPicker,
            java.awt.Color background,
            Instant obsUtc) 
    {
    	try {
    		var d = new SunSketchDbo();
    		d.id = UUID.randomUUID().toString(); 
    		d.createdUtc = obsUtc;
    		d.modifiedUtc = obsUtc;

    		d.canvas.widthPx  = sun.getWidth();
    		d.canvas.heightPx = sun.getHeight();

    		d.sunStyle.paddingFraction = sun.getPaddingFraction();
    		d.sunStyle.background = background;

    		d.sunStyle.disk.color = sun.getSunFill();
    		d.sunStyle.disk.lut.name = diskPicker.getSelectedLutName();
    		d.sunStyle.disk.lut.t    = diskPicker.getValue() / 100.0;

    		d.sunStyle.limb.color = sun.getLimbStroke();
    		d.sunStyle.limb.strokePx = 2; // or from a UI control if you add one
    		d.sunStyle.limb.lut.name = limbPicker.getSelectedLutName();
    		d.sunStyle.limb.lut.t    = limbPicker.getValue() / 100.0;

    		Document doc = SunSketchXmlMapper.toDocument(d);

    		String filename = SunSketchFileNamer.xmlName(obsUtc);
    		SunSketchXmlMapper.save(doc, folder.resolve(filename));

    	} catch (Exception ex) {
    		ex.printStackTrace();
    		javax.swing.JOptionPane.showMessageDialog(
    				null, "Failed to save sketch:\n" + ex.getMessage(),
    				"Save Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    	}
    }

    /** Quick demo frame */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var sun = new SunPanel();
            var controls = new SunControlsPanel(sun, null);

            JFrame f = new JFrame("Sun Sketching – Collapsible Controls");
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setLayout(new BorderLayout());
            f.add(sun, BorderLayout.CENTER);
            f.add(controls, BorderLayout.SOUTH); // dock controls to the side to save height
            f.setSize(1000, 700);
            f.setLocationByPlatform(true);
            f.setVisible(true);
        });
    }
    
}
