package astrogeist.ui.swing.dialog.settings;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.persitence.settings.Settings;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.ModalDialogBase;
import astrogeist.ui.swing.dialog.message.MessageDialogs;
import astrogeist.ui.swing.dialog.settings.editors.SettingsEditor;

public final class SettingsDialog extends ModalDialogBase {
    private static final long serialVersionUID = 1L;
    
    private final AstrogeistStorageManager astrogeistStorageManager;
    
    private final SettingsEditorProvider settingsEditorProvider;

    private final JTabbedPane tabs = new JTabbedPane();
    private final LinkedHashMap<String, SettingsTableModel> models = new LinkedHashMap<>();
    
    private Settings settings;

    private SettingsDialog(
    	App app,
    	AstrogeistStorageManager astrogeistStorageManager,
    	TimelineNames timelineNames) {
        super(app, "Astrogeist Settings");
        
        this.astrogeistStorageManager = astrogeistStorageManager;
        
        this.settingsEditorProvider = 
        	new SettingsEditorProvider(timelineNames);
        
        super.setSize(600, 400);
        super.setLayout(new BorderLayout());
        
        this.settings = this.loadSettings();
        buildTabs();

        add(tabs, BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }
    
    private final Settings loadSettings() {
    	try {
    		var retVal = this.astrogeistStorageManager.load(Settings.class);
    		return retVal;
    	} catch (Exception x) {
    		MessageDialogs.showError(this, "Failed to load config", x);
    		return new astrogeist.engine.persitence.settings.Settings();
    	}
    }

    private final void buildTabs() {
    	var grouped = this.settings.groupByPrefix();
        for (var entry : grouped.entrySet()) {
            String group = entry.getKey();
            SettingsTableModel model = new SettingsTableModel(entry.getValue());
            JTable table = new JTable(model);
            table.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(table);
            tabs.addTab(capitalize(group), scrollPane);

            models.put(group, model);
        }
    }

    private final JPanel buildButtons() {
    	JButton edit = new JButton("Edit");
    	edit.addActionListener(e -> onEdit());
    	
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            try {
                saveAll();
                dispose();
            } catch (Exception x) {
                MessageDialogs.showError(this, "Failed to save config", x);
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(edit);
        panel.add(save);
        panel.add(cancel);
        return panel;
    }
    
    private final void onEdit() {
        int selectedTab = tabs.getSelectedIndex();
        if (selectedTab < 0) return;

        String group = tabs.getTitleAt(selectedTab).toLowerCase(); // undo capitalize
        JScrollPane scrollPane = (JScrollPane) tabs.getComponentAt(selectedTab);
        JTable table = (JTable) scrollPane.getViewport().getView();
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) return;

        SettingsTableModel model = models.get(group);
        String key = (String) model.getValueAt(selectedRow, 0);
        String value = (String) model.getValueAt(selectedRow, 1);

        String scopedKey = group.equals("general") ? key : group + ":" + key;
        SettingsEditor editor = this.settingsEditorProvider.getEditor(scopedKey);
        var component = editor.getEditorComponent(value);

        int result = JOptionPane.showConfirmDialog(
            this,
            component,
            "Edit Setting: " + key,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newValue = editor.getEditedValue();
            model.setValueAt(newValue, selectedRow, 1);
        }
    }

    private final void saveAll() throws Exception {
        LinkedHashMap<String, LinkedHashMap<String, String>> all = new LinkedHashMap<>();
        for (var entry : models.entrySet()) {
        	var key = entry.getKey();
        	var value = entry.getValue().toMap();
            all.put(key, value);
        }
        
        this.settings.setGrouped(all);
        this.astrogeistStorageManager.save(this.settings);
        
        super.app.seetingsUpdated();
    }

    private final String capitalize(String s) { return s.substring(0, 1).toUpperCase() + s.substring(1); }
    
    public static void show(
    	App app,
    	AstrogeistStorageManager astrogeistStorageManager,
    	TimelineNames timelineNames) 
    { 
    	new SettingsDialog(app, astrogeistStorageManager, timelineNames).setVisible(true); 
    }
}
