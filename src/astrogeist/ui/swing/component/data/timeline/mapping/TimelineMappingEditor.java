package astrogeist.ui.swing.component.data.timeline.mapping;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import astrogeist.engine.resources.Resources;
import astrogeist.engine.timeline.mapping.TimelineMappingEntry;
import astrogeist.engine.timeline.mapping.TimelineMappingIo;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class TimelineMappingEditor extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private final DefaultListModel<String> fieldListModel = new DefaultListModel<>();
    private final JList<String> fieldList = new JList<>(fieldListModel);

    private final DefaultListModel<String> aliasListModel = new DefaultListModel<>();
    private final JList<String> aliasList = new JList<>(aliasListModel);

    private final Map<String, List<String>> mapping = new LinkedHashMap<>();

    public TimelineMappingEditor() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        var split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildFieldPanel(), buildAliasPanel());
        split.setResizeWeight(0.3);
        add(split, BorderLayout.CENTER);

        fieldList.addListSelectionListener(e -> refreshAliasList());
    }

    private JPanel buildFieldPanel() {
        var panel = new JPanel(new BorderLayout(5, 5));
        var label = new JLabel("Timeline Fields");
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(fieldList), BorderLayout.CENTER);

        var input = new JTextField();
        var addButton = new JButton("Add");

        addButton.addActionListener(e -> {
            var text = input.getText().trim();
            if (!text.isEmpty() && !mapping.containsKey(text)) {
                mapping.put(text, new ArrayList<>(List.of(text)));
                fieldListModel.addElement(text);
                input.setText("");
            }
        });

        var inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(input, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        var removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            var selected = fieldList.getSelectedValue();
            if (selected != null) {
                mapping.remove(selected);
                fieldListModel.removeElement(selected);
                aliasListModel.clear();
            }
        });

        var bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(removeButton, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildAliasPanel() {
        var panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Aliases"), BorderLayout.NORTH);
        panel.add(new JScrollPane(aliasList), BorderLayout.CENTER);

        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        var addAlias = new JButton("Add Alias");
        var removeAlias = new JButton("Remove");

        addAlias.addActionListener(e -> {
            String timeline = fieldList.getSelectedValue();
            if (timeline == null) return;

            String alias = JOptionPane.showInputDialog(this, "Enter alias:");
            if (alias != null && !alias.isBlank()) {
                mapping.get(timeline).add(alias.trim());
                refreshAliasList();
            }
        });

        removeAlias.addActionListener(e -> {
            String timeline = fieldList.getSelectedValue();
            List<String> aliases = mapping.get(timeline);
            List<String> selected = aliasList.getSelectedValuesList();
            if (aliases != null && selected != null) {
                aliases.removeAll(selected);
                refreshAliasList();
            }
        });

        buttonPanel.add(addAlias);
        buttonPanel.add(removeAlias);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    public void load() {
    	var file = Resources.getTimelineMappingPatternFile();
    	try {
    		
    		var entries = TimelineMappingIo.load(file);
    		this.mapping.clear();
            this.fieldListModel.clear();
            for (var entry : entries) {
                this.mapping.put(entry.timelineField(), new ArrayList<>(entry.aliases()));
                this.fieldListModel.addElement(entry.timelineField());
            }
    	} catch (Exception x) {
    		MessageDialogs.showError("Failed to load " + file.toString(), x);
    	}
    }
    
    public void save() {
    	var file = Resources.getTimelineMappingPatternFile();
    	try {
    		List<TimelineMappingEntry> entries = new ArrayList<>();
            for (var entry : mapping.entrySet()) {
                entries.add(new TimelineMappingEntry(entry.getKey(), entry.getValue()));
            }
            TimelineMappingIo.save(file, entries);
    	} catch (Exception x) {
    		MessageDialogs.showError("Failed to save " + file.toString(), x);
    	}
    }

    private void refreshAliasList() {
        aliasListModel.clear();
        String selected = fieldList.getSelectedValue();
        if (selected != null) {
            List<String> aliases = mapping.getOrDefault(selected, List.of());
            for (String alias : aliases) aliasListModel.addElement(alias);
        }
    }

    public Map<String, List<String>> getCurrentMapping() { return mapping; }
}

