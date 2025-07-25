package astrogeist.app.dialog;



import astrogeist.config.AstrogeistConfig;

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

public final class ConfigDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTabbedPane tabs = new JTabbedPane();
    private final Map<String, GroupTableModel> models = new LinkedHashMap<>();

    public ConfigDialog(Frame owner) {
        super(owner, "Astrogeist Configuration", true);
        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        try {
            var grouped = AstrogeistConfig.loadGrouped();
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
            GroupTableModel model = new GroupTableModel(entry.getValue());
            JTable table = new JTable(model);
            table.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(table);
            tabs.addTab(capitalize(group), scrollPane);

            models.put(group, model);
        }
    }

    private JPanel buildButtons() {
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

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(save);
        panel.add(cancel);
        return panel;
    }

    private void saveAll() throws IOException {
        Map<String, Map<String, String>> all = new LinkedHashMap<>();
        for (var entry : models.entrySet()) {
            all.put(entry.getKey(), entry.getValue().toMap());
        }
        AstrogeistConfig.saveGrouped(all);
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
