package astrogeist.ui.swing.component.data.timeline.view;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.abstraction.TimelineViewFilter;
import astrogeist.engine.timeline.view.CompositeFilteredTimelineView;

public final class FilteredTimelineViewTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private CompositeFilteredTimelineView view = null;
	
	public FilteredTimelineViewTableModel() {}
	
	public final CompositeFilteredTimelineView view() { return this.view; }
	
	public final void view(CompositeFilteredTimelineView view) {
		this.view = view;
		super.fireTableDataChanged();
	}
	
	public final void addFilter(TimelineViewFilter filter) {
		view.pushFilter(filter);
		super.fireTableDataChanged();
	}
	
	@Override public final int getRowCount() { return this.view == null ? 0 : this.view.getFilterCount(); }
	@Override public final int getColumnCount() { return 2; }
	@Override public final String getColumnName(int col) { return col == 0 ? "Name" : "Function"; } 
	
	@Override public final Object getValueAt(int row, int col) {
		var pos = row + 1;
		var filter = this.view.getFilter(pos);
		return col == 0 ? filter.name() : filter.toString();
	}
	
}
