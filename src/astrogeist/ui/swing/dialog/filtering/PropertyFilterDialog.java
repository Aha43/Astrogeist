package astrogeist.ui.swing.dialog.filtering;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.timeline.view.PropertyEqualsTimelineViewFilter;

public final class PropertyFilterDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
	private final JComboBox<String> cbKey;
    private final JComboBox<String> cbValue; // editable with suggestions
    private boolean ok = false;

    private PropertyFilterDialog(Window owner,
                                 Collection<String> availableKeys,
                                 Map<String, Set<String>> valuesByKey) {
        super(owner, "Property filter", ModalityType.APPLICATION_MODAL);

        cbKey = new JComboBox<>(availableKeys.stream().sorted().toArray(String[]::new));
        cbValue = new JComboBox<>();
        cbValue.setEditable(true);

        cbKey.addActionListener(e -> refreshValueSuggestions(valuesByKey));

        var form = new JPanel(new GridBagLayout());
        var g = new GridBagConstraints();
        g.insets = new Insets(4,4,4,4);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=0; form.add(new JLabel("Key"), g);
        g.gridx=1; form.add(cbKey, g);
        g.gridx=0; g.gridy=1; form.add(new JLabel("Value equals"), g);
        g.gridx=1; form.add(cbValue, g);

        var btnOk = new JButton("OK");
        var btnCancel = new JButton("Cancel");
        btnOk.addActionListener(e -> { ok = validateInput(); if (ok) setVisible(false); });
        btnCancel.addActionListener(e -> setVisible(false));

        var buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnCancel); buttons.add(btnOk);

        getContentPane().setLayout(new BorderLayout(8,8));
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);

        // initial suggestions
        refreshValueSuggestions(valuesByKey);
    }

    private void refreshValueSuggestions(Map<String, Set<String>> valuesByKey) {
        var key = (String) cbKey.getSelectedItem();
        var vals = (key == null) ? List.<String>of()
                                 : valuesByKey.getOrDefault(key, Set.of()).stream().sorted().toList();

        var model = new DefaultComboBoxModel<>(vals.toArray(String[]::new));
        cbValue.setModel(model);
        cbValue.setEditable(true);
    }

    private boolean validateInput() {
        var key = (String) cbKey.getSelectedItem();
        var val = Objects.toString(cbValue.getEditor().getItem(), "").trim();
        if (key == null || key.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please choose a key.", "Missing key", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (val.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a value.", "Missing value", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private Optional<PropertyEqualsTimelineViewFilter> result() {
        if (!ok) return Optional.empty();
        var key = (String) cbKey.getSelectedItem();
        var val = Objects.toString(cbValue.getEditor().getItem(), "").trim();
        return Optional.of(new PropertyEqualsTimelineViewFilter(key, val));
    }

    // Utilities to build suggestions from a TimelineView snapshot set (call this from your dialog opener)
    public static Map<String, Set<String>> distinctValues(Collection<Map<String, TimelineValue>> snapshots) {
        var map = new HashMap<String, Set<String>>();
        for (var snap : snapshots) {
            for (var e : snap.entrySet()) {
                map.computeIfAbsent(e.getKey(), k -> new HashSet<>()).add(e.getValue().value());
            }
        }
        return map;
    }

    // API
    public static Optional<PropertyEqualsTimelineViewFilter> show(Window owner,
                                                      Collection<String> availableKeys,
                                                      Map<String, Set<String>> valuesByKey) {
        var dlg = new PropertyFilterDialog(owner, availableKeys, valuesByKey);
        dlg.setVisible(true);
        return dlg.result();
    }

    public static Optional<PropertyEqualsTimelineViewFilter> show(Window owner,
                                                      Collection<String> availableKeys,
                                                      Map<String, Set<String>> valuesByKey,
                                                      PropertyEqualsTimelineViewFilter existing) {
        var dlg = new PropertyFilterDialog(owner, availableKeys, valuesByKey);
        dlg.cbKey.setSelectedItem(existing.key());
        dlg.cbValue.getEditor().setItem(existing.searched());
        dlg.setVisible(true);
        return dlg.result();
    }
}

