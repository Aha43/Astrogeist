package astrogeist.ui.swing.dialog.filtering;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

import astrogeist.engine.abstraction.TimelineSnapshotFilter;
import astrogeist.engine.abstraction.TimelineView;
import astrogeist.engine.timeline.filtering.FilterChain;
import astrogeist.engine.timeline.filtering.FilteredTimelineView;
import astrogeist.engine.timeline.filtering.PropertyEqualsFilter;
import astrogeist.engine.timeline.filtering.TimeRangeFilter;

public final class FilterStackDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
	private final DefaultListModel<TimelineSnapshotFilter> model = new DefaultListModel<>();
    private final JList<TimelineSnapshotFilter> list = new JList<>(model);
    private final JLabel preview = new JLabel("Matches: –");
    private final TimelineView base; // to compute preview

    private FilterStackDialog(Window owner, TimelineView base, List<TimelineSnapshotFilter> initial) {
        super(owner, "Filters", ModalityType.APPLICATION_MODAL);
        this.base = base;

        list.setCellRenderer((ListCellRenderer<? super TimelineSnapshotFilter>) (jlist, value, index, isSelected, cellHasFocus) -> {
            var r = new DefaultListCellRenderer();
            var c = r.getListCellRendererComponent(jlist, value.label(), index, isSelected, cellHasFocus);
            return c;
        });

        initial.forEach(model::addElement);

        var btnAdd = new JButton("Add…");
        var btnEdit = new JButton("Edit");
        var btnRemove = new JButton("Remove");
        var btnUp = new JButton("Up");
        var btnDown = new JButton("Down");
        var btnClear = new JButton("Clear");
        var btnApply = new JButton("Apply");
        var btnCancel = new JButton("Cancel");

        //btnAdd.addActionListener(e -> onAdd());
        //btnEdit.addActionListener(e -> onEdit());
        btnRemove.addActionListener(e -> onRemove());
        btnUp.addActionListener(e -> move(-1));
        btnDown.addActionListener(e -> move(+1));
        btnClear.addActionListener(e -> { model.clear(); refreshPreview(); });
        btnApply.addActionListener(e -> { ok = true; setVisible(false); });
        btnCancel.addActionListener(e -> setVisible(false));

        list.addListSelectionListener(e -> { /* enable/disable buttons */ });

        // layout: list center, buttons right, preview + Apply/Cancel bottom
        // (use BorderLayout + a small right-side BoxLayout panel)

        refreshPreview();
        pack();
        setLocationRelativeTo(owner);
    }

    private boolean ok = false;
    public Optional<FilterChain> showDialog() {
        setVisible(true);
        if (!ok) return Optional.empty();
        var chain = new FilterChain();
        for (int i = 0; i < model.size(); i++) chain.add(model.get(i));
        return Optional.of(chain);
    }

    /*
    private void onAdd() {
        var choice = JOptionPane.showInputDialog(this, "Add filter:", "Add", JOptionPane.PLAIN_MESSAGE, null,
                new Object[]{"Time range", "Property equals"}, "Time range");
        if (choice == null) return;
        TimelineSnapshotFilter f = switch (choice.toString()) {
            case "Time range" -> TimeRangeFilterDialog.show(this).orElse(null);
            case "Property equals" -> PropertyFilterDialog.show(this).orElse(null);
            default -> null;
        };
        if (f != null) { model.addElement(f); refreshPreview(); }
    }
    */

    /*
    private void onEdit() {
        int i = list.getSelectedIndex(); if (i < 0) return;
        var f = model.get(i);
        var edited = (f instanceof TimeRangeFilter tr)
            ? TimeRangeFilterDialog.show(this, tr)
            : (f instanceof PropertyEqualsFilter pe)
                ? PropertyFilterDialog.show(this, pe)
                : Optional.<TimelineSnapshotFilter>empty();
        edited.ifPresent(newF -> { model.set(i, newF); refreshPreview(); });
    }
    */

    private void onRemove() {
        int i = list.getSelectedIndex(); if (i >= 0) { model.remove(i); refreshPreview(); }
    }

    private void move(int delta) {
        int i = list.getSelectedIndex(); if (i < 0) return;
        int j = i + delta; if (j < 0 || j >= model.size()) return;
        var f = model.get(i);
        model.remove(i);
        model.add(j, f);
        list.setSelectedIndex(j);
        refreshPreview();
    }

    private void refreshPreview() {
        var filters = new ArrayList<TimelineSnapshotFilter>();
        for (int i = 0; i < model.size(); i++) filters.add(model.get(i));
        TimelineView view = filters.isEmpty() ? base : new FilteredTimelineView(base, filters);
        preview.setText("Matches: " + view.timestamps().size());
    }
    
}
