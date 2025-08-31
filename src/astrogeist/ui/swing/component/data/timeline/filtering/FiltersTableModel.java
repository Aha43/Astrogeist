package astrogeist.ui.swing.component.data.timeline.filtering;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.abstraction.TimelineViewFilter;

/**
 * <p>
 *   Table model that shows filters and with API to add and remove filters.
 * </p>
 */
public final class FiltersTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private  FilteredTimelineViewTableModel filters = null;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param filters The table model that shows the filtered time line. This uses its API to add and remove filters.
	 */
	public FiltersTableModel(FilteredTimelineViewTableModel filters) { this.filters = filters; }
	
	public final void clearFilters() {
		this.filters.clearFilters();
		super.fireTableDataChanged();
	}
	
	public final void pushFilter(TimelineViewFilter filter) {
		this.filters.pushFilter(filter);
		super.fireTableDataChanged();
	}
	
	public final int getFilterCount() { return this.getRowCount(); }
	
	@Override public final int getRowCount() { return this.filters == null ? 0 : this.filters.getFilterCount(); }
	@Override public final int getColumnCount() { return 2; }
	@Override public final String getColumnName(int col) { return col == 0 ? "Name" : "Function"; } 
	
	@Override public final Object getValueAt(int row, int col) {
		var filter = this.filters.getFilter(row);
		return col == 0 ? filter.name() : filter.toString();
	}
	
}
