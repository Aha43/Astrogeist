package astrogeist.ui.swing.tool.sun.sketching;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import astrogeist.ui.swing.component.general.CollapsibleSection;
import astrogeist.ui.swing.tool.component.UtcInstantField;

public final class SunControlsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final SunPanel sun;
    private final Path saveFolder;

    // keep references so Save can read current choices
    private final RampColorPicker bgPicker;
    private final RampColorPicker dlPicker;
    private final UtcInstantField utcField;

    public SunControlsPanel(SunPanel sun, Path saveFolder) {
        this.sun = sun;
        this.saveFolder = saveFolder;

        setLayout(new BorderLayout());
        var root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JScrollPane(root,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        ), BorderLayout.CENTER);

        // --- Layout section (always visible)
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

        // --- Collapsible: Background (greyscale)
        var bgPanel = new JPanel(new GridBagLayout());
        var gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(6, 6, 6, 6);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1;

        bgPicker = new RampColorPicker();
        bgPicker.setSelectedLut("Greyscale");
        bgPicker.setListener(sun::setBackgroundColor);

        gbc2.gridx = 0; gbc2.gridy = 0; gbc2.gridwidth = 1;
        bgPanel.add(new JLabel("Background"), gbc2);
        gbc2.gridx = 1; gbc2.gridy = 0; gbc2.gridwidth = 2;
        bgPanel.add(bgPicker, gbc2);

        var bgSection = new CollapsibleSection("Background Color", bgPanel, false);
        root.add(bgSection);
        root.add(Box.createVerticalStrut(6));

        // --- Collapsible: Disk & Limb (LUT + intensity)
        var dlPanel = new JPanel(new GridBagLayout());
        var gbc3 = new GridBagConstraints();
        gbc3.insets = new Insets(6, 6, 6, 6);
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.weightx = 1;

        dlPicker = new RampColorPicker();

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
        root.add(Box.createVerticalStrut(6));

        // --- Save section (always visible)
        var savePanel = new JPanel(new GridBagLayout());
        var gbc4 = new GridBagConstraints();
        gbc4.insets = new Insets(6, 6, 6, 6);
        gbc4.fill = GridBagConstraints.HORIZONTAL;
        gbc4.weightx = 1;

        // UTC instant field defaults to "now"
        utcField = new UtcInstantField(Instant.now());

        var folderLabel = new JLabel("Folder:");
        var folderValue = new JTextField(saveFolder.toString());
        folderValue.setEditable(false);
        folderValue.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        var saveBtn = new JButton("Save XML");
        saveBtn.addActionListener(e -> onSave());

        gbc4.gridx = 0; gbc4.gridy = 0; gbc4.gridwidth = 1; savePanel.add(utcField, gbc4);
        gbc4.gridx = 0; gbc4.gridy = 1; gbc4.gridwidth = 1; savePanel.add(folderLabel, gbc4);
        gbc4.gridx = 1; gbc4.gridy = 1; gbc4.gridwidth = 2; savePanel.add(folderValue, gbc4);

        gbc4.gridx = 1; gbc4.gridy = 2; gbc4.gridwidth = 2; savePanel.add(saveBtn, gbc4);

        savePanel.setBorder(BorderFactory.createTitledBorder("Save"));
        root.add(savePanel);
    }

    private void onSave() {
        try {
            var d = new SunSketchDbo();
            
            d.id = UUID.randomUUID().toString();
            var utc = utcField.getInstant();
            d.createdUtc = utc;
            d.modifiedUtc = utc;
            
            d.canvas.widthPx  = sun.getWidth();
            d.canvas.heightPx = sun.getHeight();

            d.sunStyle.paddingFraction = sun.getPaddingFraction();
            d.sunStyle.background = sun.getBackgroundColor();

            d.sunStyle.disk.color = sun.getSunFill();
            d.sunStyle.disk.lut.name = dlPicker.getSelectedLutName();
            d.sunStyle.disk.lut.t    = dlPicker.getValue() / 100.0;

            d.sunStyle.limb.color = sun.getLimbStroke();
            d.sunStyle.limb.lut.name = dlPicker.getSelectedLutName();
            d.sunStyle.limb.lut.t    = dlPicker.getValue() / 100.0;

            // --- NEW: copy sunspots (normalized units) ---
            var spots = sun.getSunspots(); // List<SunPanel.Sunspot>
            var counter = new AtomicInteger(1);
            for (var s : spots) {
                var ds = new SunSketchDbo.Sunspot();
                ds.id = "spot-" + counter.getAndIncrement(); // stable, human-readable ids
                ds.group = null; // (optional) fill later when you add a group tool
                ds.angleDeg = s.angleDeg;
                ds.rho      = s.rho;
                ds.sizeR    = s.sizeR;
                d.features.sunspots.add(ds);
            }

            // Serialize & save
            var doc = SunSketchXmlMapper.toDocument(d);
            var filename = SunSketchFileNamer.xmlName(utcField.getInstant());
            SunSketchXmlMapper.save(doc, saveFolder.resolve(filename));

            JOptionPane.showMessageDialog(this,
                "Saved:\n" + saveFolder.resolve(filename),
                "Sun Sketch Saved", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Failed to save sketch:\n" + ex.getMessage(),
                "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
