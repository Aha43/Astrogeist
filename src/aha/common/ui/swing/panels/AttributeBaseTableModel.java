package aha.common.ui.swing.panels;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import aha.common.util.AttributeBase;

// Internal table adapter class used by AttributeBasePanel.

final class AttributeBaseTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private AttributeBase<?> data = null;
	private List<String> names = null;
	
	public AttributeBaseTableModel() {}
	
	public AttributeBaseTableModel(AttributeBase<?> data) { this.data(data); }
	
	public final void data(AttributeBase<?> data) {
		this.data = requireNonNull(data);
		this.names = data.names();
		super.fireTableDataChanged();
	}
	
	public final void clear() { 
		this.data = null; super.fireTableDataChanged(); } 

	@Override public final int getRowCount() {
		return this.data == null ? 0 : this.names.size(); }

	@Override public final int getColumnCount() { return 2; }

	@Override public final Object getValueAt(int row, int col) {
		var name = this.names.get(row);
		var retVal = col == 0 ? name : data.get(name);
		return retVal;
	}
	
	@Override public final String getColumnName(int col) {
		return col == 0 ? "Name" : "Value"; }
}
