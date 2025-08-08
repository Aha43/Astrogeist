package astrogeist.ui.swing.component.data.userdata;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;
import astrogeist.engine.userdata.UserDataDefinition;
import astrogeist.ui.swing.component.data.timeline.value.TimelineValueRenderers;

public final class UserDataEditor extends JPanel {
    private static final long serialVersionUID = 1L;
	private final JTable table;
    private final UserDataDefinitionsTableModel model;

    public UserDataEditor(List<UserDataDefinition> definitions, LinkedHashMap<String, TimelineValue> currentValues) {
        super(new BorderLayout());
        this.model = new UserDataDefinitionsTableModel(definitions, currentValues);
        this.table = new JTableWithPerRowEditor(model, definitions);
        this.table.setFillsViewportHeight(true);
        
        this.table.setDefaultRenderer(TimelineValue.class, TimelineValueRenderers.tableCellRenderer());

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public LinkedHashMap<String, TimelineValue> getValues() { return model.getValues(); }

    // Custom JTable that installs per-row editors
    private static final class JTableWithPerRowEditor extends JTable {
        private static final long serialVersionUID = 1L;
		private final List<UserDataDefinition> defs;

        public JTableWithPerRowEditor(UserDataDefinitionsTableModel model, List<UserDataDefinition> defs) {
            super(model);
            this.defs = defs;
        }
        
        @Override
        public TableCellEditor getCellEditor(int row, int col){
        	if (col != 1) return super.getCellEditor();
        	
        	var def = defs.get(row);
        	
        	if (def.values().isEmpty()) {
        		var textField = new JTextField();
        	    return new DefaultCellEditor(textField) {
        	        private static final long serialVersionUID = 1L;
					@Override
        	        public Object getCellEditorValue() {
        	            String text = textField.getText().trim();
        	            return new TimelineValue(text, Type.Text());
        	        }
        	    };
        	}
        	
        	var comboValues = new ArrayList<TimelineValue>();
        	comboValues.add(TimelineValue.Empty);
        	def.values().forEach(v -> comboValues.add(new TimelineValue(v, Type.Text())));
        	var combo = new JComboBox<>(comboValues.toArray(TimelineValue.EmptyArray));
        	combo.setRenderer(TimelineValueRenderers.listCellRenderer());
        	return new DefaultCellEditor(combo) {
        	    private static final long serialVersionUID = 1L;

        	    @Override
        	    public Object getCellEditorValue() {
        	        return combo.getSelectedItem(); // This returns the actual TimelineValue
        	    }
        	};
        }
    }

    private static final class UserDataDefinitionsTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        
		private final List<UserDataDefinition> defs;
        private final LinkedHashMap<String, TimelineValue> values;

        public UserDataDefinitionsTableModel(List<UserDataDefinition> defs, LinkedHashMap<String, TimelineValue> initialValues) {
            this.defs = defs;
            this.values = new LinkedHashMap<>();
            for (var def : defs) {
                var name = def.name();
                var initialValue = initialValues.getOrDefault(name, TimelineValue.Empty); 
                values.put(name, initialValue);
            }
        }

        @Override public int getRowCount() { return defs.size(); }
        @Override public int getColumnCount() { return 2; }

        @Override public Object getValueAt(int row, int col) {
            var def = defs.get(row);
            
            if (col == 0) return def.name();
            
            var tlv = values.get(def.name());
            return tlv;
        }

        @Override public void setValueAt(Object value, int row, int col) {
            if (col != 1) return;
            
            var def = defs.get(row);
                
            if (value instanceof TimelineValue tlv) {
            	values.put(def.name(), tlv);
                return;
            }
                
            var svalue = value == null ? "" : value.toString().trim();
            var tlv = svalue.length() == 0 ? TimelineValue.Empty : new TimelineValue(svalue, Type.Text());
                
            values.put(def.name(), tlv);
        }

        @Override public boolean isCellEditable(int row, int col) { return col == 1; }
        @Override public String getColumnName(int col) { return (col == 0) ? "Name" : "Value"; }
        @Override public Class<?> getColumnClass(int col) { return (col == 0) ? String.class : TimelineValue.class; }

        public LinkedHashMap<String, TimelineValue> getValues() {
            LinkedHashMap<String, TimelineValue> cleaned = new LinkedHashMap<>();
            for (var def : defs) {
                String name = def.name();
                TimelineValue tvl = values.get(name);
                if (tvl != null && tvl.value() != null && !tvl.value().isBlank()) {
                    cleaned.put(name, tvl);
                }
            }
            return cleaned;
        }
    }
    
}
