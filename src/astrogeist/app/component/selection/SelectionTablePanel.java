package astrogeist.app.component.selection;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public final class SelectionTablePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
	private final JTable table;
    private final SelectionTableModel model;

    public SelectionTablePanel(List<String> selectedItems, Collection<String> allItems) {
        setLayout(new BorderLayout());
        model = new SelectionTableModel(selectedItems, allItems);
        table = new JTable(model);
        table.setFillsViewportHeight(true);

        // Make checkbox column editable
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public List<String> getSelectedItems() {
        return model.getSelectedItems();
    }

    // Inner model class
    private static class SelectionTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        
		private final List<String> allItems;
        private final Map<String, Boolean> selectionMap;

        public SelectionTableModel(List<String> selectedItems, Collection<String> allItems) {
            this.allItems = new ArrayList<>(allItems);
            this.selectionMap = new LinkedHashMap<>();
            Set<String> selectedSet = new HashSet<>(selectedItems);

            for (String item : allItems) {
                selectionMap.put(item, selectedSet.contains(item));
            }
        }

        @Override
        public int getRowCount() { return this.allItems.size(); }

        @Override
        public int getColumnCount() { return 2; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var item = this.allItems.get(rowIndex);
            return columnIndex == 0 ? item : this.selectionMap.get(item);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1 && aValue instanceof Boolean) {
                String item = allItems.get(rowIndex);
                selectionMap.put(item, (Boolean) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 1 ? Boolean.class : String.class;
        }

        @Override
        public String getColumnName(int column) {
            return column == 0 ? "Item" : "Visible";
        }

        public List<String> getSelectedItems() {
            List<String> selected = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : selectionMap.entrySet()) {
                if (entry.getValue()) {
                    selected.add(entry.getKey());
                }
            }
            return selected;
        }
    }
}

