package astrogeist.app.dialog.settings;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import astrogeist.setting.Settings;
import astrogeist.setting.SettingsIO;

public final class SettingsDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTabbedPane tabs = new JTabbedPane();
    private final Map<String, SettingsTableModel> models = new LinkedHashMap<>();

    public SettingsDialog(Frame owner) {
        super(owner, "Astrogeist Settings", true);
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        try {
            var grouped = SettingsIO.loadGrouped();
            buildTabs(grouped);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load config: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(tabs, BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
    }

    private void buildTabs(Map<String, Map<String, String>> grouped) {
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
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save config: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        SettingsEditor editor = SettingsEditorProvider.getEditor(scopedKey);
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

    private void saveAll() throws IOException {
        Map<String, Map<String, String>> all = new LinkedHashMap<>();
        for (var entry : models.entrySet()) {
            all.put(entry.getKey(), entry.getValue().toMap());
        }
        SettingsIO.saveGrouped(all);
        Settings.load();
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
