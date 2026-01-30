package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.SwingUtilities.isEventDispatchThread;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.Match;

/**
 * <p>
 *   Displays configuration match results in a JTable.
 * </p>
 * <p>
 *   Columns:
 *   - Name
 *   - Missing (#)
 *   - Extra (#)
 *   - Jaccard
 * </p>
 */
public final class ConfigurationMatchTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final MatchTableModel model = new MatchTableModel();
	private final JTable table = new JTable(model);
	private final JLabel headerLabel = new JLabel("Configurations");

	private final List<MatchSelectionListener> listeners = new ArrayList<>();
	//private boolean programmaticSelectionChange = false;

	public ConfigurationMatchTablePanel() {
		super(new BorderLayout(6, 6));
		setBorder(BorderFactory.createTitledBorder("Matching configurations"));

		headerLabel.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
		add(headerLabel, BorderLayout.NORTH);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);

		// Reasonable default column widths
		table.getColumnModel().getColumn(0).setPreferredWidth(260); // Name
		table.getColumnModel().getColumn(1).setPreferredWidth(70);  // Missing
		table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Extra
		table.getColumnModel().getColumn(3).setPreferredWidth(70);  // Jaccard

		add(new JScrollPane(table), BorderLayout.CENTER);

		table.getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) return;
				//if (programmaticSelectionChange) return;

				int row = table.getSelectedRow();
				var selected = (row >= 0) ? model.getAt(row) : null;
				fireSelectionChanged(selected);
			}
		});
	}

	/** 
	 * <p>
	 *   Updates the table with a new match list.
	 *   Optionally sets header text (e.g. "Showing all").
	 * </p>
	 */
	public final void setMatches(String headerText,
		List<Match> matches) {
    
		requireNonNull(matches, "matches");
		headerLabel.setText(Objects.requireNonNullElse(headerText, 
			"Configurations"));
		model.setMatches(matches);

		// Auto-select first row when results exist
		//programmaticSelectionChange = true;
		//try {
		//	if (!matches.isEmpty()) table.setRowSelectionInterval(0, 0);
		//	else                    table.clearSelection();
		//} finally {
		//	programmaticSelectionChange = false;
		//}

		fireSelectionChanged(model.getRowCount() > 0 ? model.getAt(0) : null);
	}

	public final Match getSelectedMatch() {
		int row = table.getSelectedRow();
		return row >= 0 ? model.getAt(row) : null;
	}

	public final void addSelectionListener(MatchSelectionListener l) {
		listeners.add(requireNonNull(l, "listener")); }

	public final void removeSelectionListener(MatchSelectionListener l) {
		listeners.remove(requireNonNull(l, "listener")); }

	private void fireSelectionChanged(Match selected) {
		if (isEventDispatchThread()) {
			for (var l : listeners) l.selectionChanged(selected);
		} else {
			invokeLater(() -> {
				for (var l : listeners) l.selectionChanged(selected);
			});
		}
	}

	// ---- Table model ----

	private static final class MatchTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;

		private static final String[] COLS = 
			{ "Name", "Missing", "Extra", "Jaccard" };

		private List<Match> matches = List.of();

		void setMatches(List<Match> matches) {
			this.matches = List.copyOf(matches);
			fireTableDataChanged();
		}

		Match getAt(int row) { return matches.get(row); }

		public int getRowCount() { return matches.size(); }
		public int getColumnCount() { return COLS.length; }
		public String getColumnName(int col) { return COLS[col]; }

		public Class<?> getColumnClass(int col) {
			return switch (col) {
				case 0 -> String.class;
				case 1, 2 -> Integer.class;
				case 3 -> Double.class;
				default -> Object.class;
			};
		}

		public Object getValueAt(int row, int col) {
			Match m = matches.get(row);
			Configuration c = m.configuration();
			return switch (col) {
				case 0 -> c.name();
				case 1 -> m.missing().size();
				case 2 -> m.extra().size();
				case 3 -> round3(m.jaccard());
				default -> "";
			};
		}

		private static double round3(double v) {
			return Math.round(v * 1000.0) / 1000.0; }
	}
	
}

