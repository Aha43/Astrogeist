package astrogeist.ui.swing.component.data.timeline.filtering;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.timeline.view.filters.AbstractCompositeTimelineFilter;

public final class FilterLayerTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private AbstractCompositeTimelineFilter filter = null;
	
	public FilterLayerTableModel() {}
	public FilterLayerTableModel(AbstractCompositeTimelineFilter filter) {
		this.filter = filter; }
	
	
	public final void filter(AbstractCompositeTimelineFilter filter) { 
		this.filter = filter;
		super.fireTableDataChanged();
	}
	
	public final AbstractCompositeTimelineFilter filter() { return this.filter; }
	
	
	@Override public final int getRowCount() { 
		return this.filter == null ? 0 : this.filter.size(); }
	
	@Override public final int getColumnCount() { return 2; }
	
	@Override public final Object getValueAt(int row, int col) {
		var filter = this.filter.get(row);
		return col == 0 ? filter.name() : filter.toString();
	}
	
	@Override public final String getColumnName(int col) {
		return col == 0 ? "Name" : "Filter"; }

}
