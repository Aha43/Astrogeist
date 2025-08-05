package astrogeist.ui.swing.dialog.settings.editors;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class PathListEditor implements SettingsEditor {

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> list = new JList<>(listModel);
    private final JPanel panel = new JPanel(new BorderLayout());

    public PathListEditor() {
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add...");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(this::onAddClicked);
        removeButton.addActionListener(e -> {
            int selected = list.getSelectedIndex();
            if (selected != -1) listModel.remove(selected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        // List setup
        JScrollPane scrollPane = new JScrollPane(list);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public JComponent getEditorComponent(String currentValue) {
        listModel.clear();
        if (currentValue != null && !currentValue.isBlank()) {
            String[] paths = currentValue.split(File.pathSeparator);
            for (String path : paths) {
                listModel.addElement(path.trim());
            }
        }
        return panel;
    }

    @Override
    public String getEditedValue() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            values.add(listModel.get(i));
        }
        return String.join(File.pathSeparator, values);
    }

    private void onAddClicked(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        int result = chooser.showOpenDialog(panel);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selected = chooser.getSelectedFiles();
            for (File file : selected) {
                String path = file.getAbsolutePath();
                if (!listModel.contains(path)) {
                    listModel.addElement(path);
                }
            }
        }
    }
}
