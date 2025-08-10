package astrogeist.ui.swing.dialog.logging;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import astrogeist.engine.logging.Log;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.DialogBase;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class LoggingControlDialog extends DialogBase {
    private static final long serialVersionUID = 1L;
    
	private static final Map<String, Level> LEVELS = new LinkedHashMap<>();
    static {
        LEVELS.put("OFF",    Level.OFF);
        LEVELS.put("ERROR",  Level.SEVERE);
        LEVELS.put("WARN",   Level.WARNING);
        LEVELS.put("INFO",   Level.INFO);
        LEVELS.put("DEBUG",  Level.FINE);
        LEVELS.put("TRACE",  Level.FINER);
        LEVELS.put("ALL",    Level.ALL);
    }

    private final JComboBox<String> levelCombo = new JComboBox<>(LEVELS.keySet().toArray(new String[0]));
    private final JCheckBox fileEnable = new JCheckBox("Log to file");
    private final JCheckBox fileAppend = new JCheckBox("Append");
    private final JTextField filePath = new JTextField(28);
    private final JButton browse = new JButton("Browse…");
    private final JButton apply = new JButton("Apply");
    private final JButton close = new JButton("Close");
    private final JButton mark = new JButton("Insert Marker…");
    private final JButton test = new JButton("Write test logs");

    private LoggingControlDialog(App app) {
        super(app, "Logging", false);

        // initial state
        levelCombo.setSelectedItem(LEVELS.entrySet().stream()
                .filter(e -> e.getValue().equals(Log.getGlobalLevel()))
                .map(Map.Entry::getKey).findFirst().orElse("WARN"));
        fileEnable.setSelected(Log.isFileLoggingEnabled());
        fileAppend.setSelected(true);

        // layout
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);

        c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.LINE_END;
        p.add(new JLabel("Level:"), c);
        c.gridx=1; c.anchor=GridBagConstraints.LINE_START;
        p.add(levelCombo, c);

        c.gridx=0; c.gridy=1; c.gridwidth=2; c.anchor=GridBagConstraints.LINE_START;
        p.add(fileEnable, c);

        JPanel fp = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        fp.add(new JLabel("File:"));
        fp.add(filePath);
        fp.add(browse);
        fp.add(fileAppend);

        c.gridx=0; c.gridy=2; c.gridwidth=2; c.fill=GridBagConstraints.HORIZONTAL;
        p.add(fp, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(mark);
        buttons.add(test);
        buttons.add(apply);
        buttons.add(close);

        c.gridx=0; c.gridy=3; c.gridwidth=2; c.fill=GridBagConstraints.NONE;
        p.add(buttons, c);

        setContentPane(p);
        pack();
        setLocationRelativeTo(app.getFrame());

        // actions
        browse.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Choose log file");
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                filePath.setText(fc.getSelectedFile().getAbsolutePath());
            }
        });

        apply.addActionListener(e -> applyChanges());
        mark.addActionListener(e -> insertMarker());
        test.addActionListener(e -> writeTestLogs());
        close.addActionListener(e -> dispose());

        updateEnabledState();
        fileEnable.addActionListener(e -> updateEnabledState());
    }

    private void updateEnabledState() {
        boolean en = fileEnable.isSelected();
        filePath.setEnabled(en);
        browse.setEnabled(en);
        fileAppend.setEnabled(en);
    }

    private void applyChanges() {
        Level level = LEVELS.getOrDefault((String) levelCombo.getSelectedItem(), Level.WARNING);
        Log.setGlobalLevel(level);

        if (fileEnable.isSelected() && !filePath.getText().isBlank()) {
            try {
                Log.enableFileLogging(Path.of(filePath.getText()), fileAppend.isSelected());
            } catch (Exception ex) {
            	MessageDialogs.showError(this, "Failed to enable file logging", ex);
                return;
            }
        } else {
            Log.disableFileLogging();
        }

        MessageDialogs.showInfo(this, "Logging settings applied");
    }
    
    private void insertMarker() {
    	var label = MessageDialogs.prompt(this, "Inser Marker", "Marker label:");
        if (label == null) return; // canceled

        String ts = java.time.ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String line = "\n========== MARKER: " + label + " @ " + ts + " ==========\n";

        // Log at the level currently selected in the dialog so it’s visible without changing levels
        java.util.logging.Level sel = LEVELS.getOrDefault(
                (String) levelCombo.getSelectedItem(),
                Log.getGlobalLevel()
        );

        java.util.logging.Logger logger = Log.get("marker");
        logger.log(sel, line);
    }

    private void writeTestLogs() {
        Log.error("SEVERE: something failed");
        Log.warn("WARNING: caution");
        Log.info("INFO: useful during debugging");
        Log.debug("DEBUG (FINE): detailed dev log");
        MessageDialogs.showInfo(this, "Logging settings applied");
    }
    
    private static DialogBase _dialog;
    public static void show(App app) { 
    	if (_dialog == null) _dialog = new LoggingControlDialog(app);
    	_dialog.showIt();
    }
}
