package aha.common.ui.swing.panels.namevalueunit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public final class NameValueUnitPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final NameValueUnitTableModel model =
		new NameValueUnitTableModel();
    
	private final JTable table = new JTable(model);

    public NameValueUnitPanel() {
        super(new BorderLayout());

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        var scrollPane = new JScrollPane(table);
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

    private void onAdd() {
        var parentWindow = SwingUtilities.getWindowAncestor(this);
        var entry = NameValueUnitDialog.showDialog(parentWindow, null);
        if (entry != null) {
            model.addEntry(entry);
            int lastRow = model.getRowCount() - 1;
            if (lastRow >= 0) {
                table.setRowSelectionInterval(lastRow, lastRow);
                table.scrollRectToVisible(table.getCellRect(lastRow, 0, true));
            }
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Select a row to edit.",
                "No selection",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        var original = model.entryAt(row);
        // You can pass the original directly; we create a new instance in 
        // dialog on OK.
        var parentWindow = SwingUtilities.getWindowAncestor(this);
        var edited = NameValueUnitDialog.showDialog(parentWindow, original);
        if (edited != null) {
            model.updateEntry(row, edited);
        }
    }

    private void onRemove() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Select a row to remove.",
                "No selection",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        var confirm = JOptionPane.showConfirmDialog(
            this,
            "Remove selected row?",
            "Confirm removal",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (confirm == JOptionPane.OK_OPTION) {
            model.removeEntry(row);
        }
    }

    // Useful API for the outside world

    public final List<NameValueUnitEntry> entries() { return model.entries(); }

    public final void entries(List<NameValueUnitEntry> entries) {
        model.entries(entries); }
}
