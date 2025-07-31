package astrogeist.app.component.userdata;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import astrogeist.userdata.UserDataDefinition;

public final class UserDataEditor extends JPanel {
    private static final long serialVersionUID = 1L;
	private final JTable table;
    private final UserDataDefinitionsTableModel model;

    public UserDataEditor(List<UserDataDefinition> definitions, Map<String, String> currentValues) {
        super(new BorderLayout());
        this.model = new UserDataDefinitionsTableModel(definitions, currentValues);
        this.table = new JTableWithPerRowEditor(model, definitions);
        this.table.setFillsViewportHeight(true);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public Map<String, String> getValues() { return model.getValues(); }

    // Custom JTable that installs per-row editors
    private static final class JTableWithPerRowEditor extends JTable {
        private static final long serialVersionUID = 1L;
		private final List<UserDataDefinition> defs;

        public JTableWithPerRowEditor(UserDataDefinitionsTableModel model, List<UserDataDefinition> defs) {
            super(model);
            this.defs = defs;
        }

        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            if (column == 1) {
                UserDataDefinition def = defs.get(row);
                if (!def.values().isEmpty()) {
                    JComboBox<String> combo = new JComboBox<>(def.values().toArray(new String[0]));
                    return new DefaultCellEditor(combo);
                }
            }
            return super.getCellEditor(row, column);
        }
    }

    private static final class UserDataDefinitionsTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        
		private final List<UserDataDefinition> defs;
        private final Map<String, String> values;

        public UserDataDefinitionsTableModel(List<UserDataDefinition> defs, Map<String, String> initialValues) {
            this.defs = defs;
            this.values = new LinkedHashMap<>();
            for (var def : defs) {
                String name = def.name();
                values.put(name, initialValues.getOrDefault(name, ""));
            }
        }

        @Override public int getRowCount() { return defs.size(); }
        @Override public int getColumnCount() { return 2; }

        @Override public Object getValueAt(int row, int col) {
            var def = defs.get(row);
            return (col == 0) ? def.name() : values.get(def.name());
        }

        @Override public void setValueAt(Object value, int row, int col) {
            if (col == 1) {
                var def = defs.get(row);
                values.put(def.name(), value != null ? value.toString().trim() : "");
            }
        }

        @Override public boolean isCellEditable(int row, int col) { return col == 1; }
        @Override public String getColumnName(int col) { return (col == 0) ? "Property" : "Value"; }

        public Map<String, String> getValues() {
            Map<String, String> cleaned = new LinkedHashMap<>();
            for (var def : defs) {
                String name = def.name();
                String val = values.get(name);
                if (val != null && !val.isBlank()) {
                    cleaned.put(name, val);
                }
            }
            return cleaned;
        }
    }
}
