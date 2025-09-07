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

import astrogeist.engine.setting.Settings;
import astrogeist.engine.setting.SettingsIo;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.ModalDialogBase;
import astrogeist.ui.swing.dialog.message.MessageDialogs;
import astrogeist.ui.swing.dialog.settings.editors.SettingsEditor;

public final class SettingsDialog extends ModalDialogBase {
    private static final long serialVersionUID = 1L;
    
    private final SettingsEditorProvider settingsEditorProvider;

    private final JTabbedPane tabs = new JTabbedPane();
    private final LinkedHashMap<String, SettingsTableModel> models = new LinkedHashMap<>();

    private SettingsDialog(App app) {
        super(app, "Astrogeist Settings");
        
        this.settingsEditorProvider = 
        	new SettingsEditorProvider(app.getServices().getTimelineNames());
        
        super.setSize(600, 400);
        super.setLayout(new BorderLayout());

        try {
            var grouped = SettingsIo.loadGrouped();
            buildTabs(grouped);
        } catch (Exception x) {
            MessageDialogs.showError(this, "Failed to load config", x);
        }

        add(tabs, BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    private void buildTabs(LinkedHashMap<String, LinkedHashMap<String, String>> grouped) {
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

    private JPanel buildButtons() {
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
    
    private void onEdit() {
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

    private void saveAll() throws Exception {
        LinkedHashMap<String, LinkedHashMap<String, String>> all = new LinkedHashMap<>();
        for (var entry : models.entrySet()) {
        	var key = entry.getKey();
        	var value = entry.getValue().toMap();
            all.put(key, value);
        }
        SettingsIo.saveGrouped(all);
        Settings.load();
        super.app.seetingsUpdated();
    }

    private String capitalize(String s) { return s.substring(0, 1).toUpperCase() + s.substring(1); }
    
    public static void show(App app) { new SettingsDialog(app).setVisible(true); }
}
