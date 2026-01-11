package aha.common.ui.swing.panels.genericlist;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import static java.util.Objects.requireNonNull;

/**
 * <p>
 *   Generic panel showing a JList&lt;T&gt; with Add/Edit/Delete buttons.
 * </p>
 * <p>
 *   The list uses {@link Object#toString()} for rendering.
 *   Actual creation / editing / deletion is delegated to a
 *   {@link ListItemHandler}.
 * </p>
 */
public final class GenericObjectListPanel<T> extends JPanel {
    private static final long serialVersionUID = 1L;
	
    private final DefaultListModel<T> listModel = new DefaultListModel<>();
    private final JList<T> list = new JList<>(listModel);
    private final ListItemHandler<T> handler;

    public GenericObjectListPanel(ListItemHandler<T> handler) {
        super(new BorderLayout());
        
        this.handler = requireNonNull(handler, "handler");

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Default renderer uses toString()
        list.setCellRenderer(new DefaultListCellRenderer());

        var scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);

        var addButton = new JButton("Add");
        var editButton = new JButton("Edit");
        var removeButton = new JButton("Remove");

        addButton.addActionListener(e -> onAdd());
        editButton.addActionListener(e -> onEdit());
        removeButton.addActionListener(e -> onRemove());

        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private final void onAdd() {
        var parentWindow = SwingUtilities.getWindowAncestor(this);
        var item = handler.createNew(parentWindow);
        if (item != null) {
            listModel.addElement(item);
            int index = listModel.size() - 1;
            if (index >= 0) {
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    private final void onEdit() {
        int index = list.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Select an item to edit.",
                "No selection",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        var existing = listModel.getElementAt(index);
        var parentWindow = SwingUtilities.getWindowAncestor(this);
        var updated = handler.edit(parentWindow, existing);

        if (updated != null) {
            listModel.set(index, updated);
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }

    private final void onRemove() {
        int index = list.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Select an item to remove.",
                "No selection",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        var existing = listModel.getElementAt(index);
        var parentWindow = SwingUtilities.getWindowAncestor(this);
        boolean shouldDelete = handler.confirmDelete(parentWindow, existing);

        if (shouldDelete) {
            listModel.remove(index);
        }
    }

    // --- Public API ---

    public final void setItems(List<T> items) {
        listModel.clear();
        if (items != null) {
            for (T item : items) {
                listModel.addElement(item);
            }
        }
    }

    public final List<T> getItems() {
        return Collections.list(listModel.elements());
    }

    public final T getSelectedItem() { return list.getSelectedValue(); }

    public final void setSelectedItem(T item) {
    	list.setSelectedValue(item, true); }

    public final JList<T> getList() { return list; }
}

