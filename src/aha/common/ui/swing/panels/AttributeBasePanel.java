package aha.common.ui.swing.panels;

import static java.util.Objects.requireNonNull;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import aha.common.util.AttributeBase;

/**
 * <p>
 *   Shows the value of an
 *   {@link AttributeBase} in a two column table: names in first column and 
 *   values in second.
 * </p>
 */
public final class AttributeBasePanel extends JPanel {
	private static final long serialVersionUID = 17751852767793219L;

	private final AttributeBaseTableModel model = new AttributeBaseTableModel();
	private final JTable table = new JTable(this.model);
	
	/**
	 * <p>
	 *   Constructs empty table view.
	 * </p>
	 */
	public AttributeBasePanel() {
		super.setLayout(new BorderLayout());
		super.add(new JScrollPane(this.table), BorderLayout.CENTER);
		this.table.setDefaultRenderer(Object.class,
			TheTableCellrenderer.INSTANCE);
	}
	
	/**
	 * <p>
	 *   Construct table view with content.
	 * </p>
	 * @param data the data to show.
	 * @throws NullPointerException if {@code data} is {@code null}. 
	 */
	public AttributeBasePanel(AttributeBase<?> data) { 
		this(); this.data(data); }
	
	/**
	 * <p>
	 *   Sets the data to show.
	 * </p>
	 * @param data the data.
	 * @throws NullPointerException if {@code data} is {@code null} (use
	 * {@link #clear()} to remove data shown.
	 */
	public void data(AttributeBase<?> data) { 
		this.model.data(requireNonNull(data, "data")); }
	
	/**
	 * <p>
	 *   Removes data shown.
	 * </p>
	 */
	public void clear() { this.model.clear(); }
	
	/**
	 * <p>
	 *   Invoke dialog showing the panel with given 
	 *   {@link AttributeBase} object.
	 * </p>
	 * <p>
	 *   Mostly for testing / demo purpose.
	 * </p>
	 * @param o the object to show.
	 */
	public static void showDialog(AttributeBase<?> o) {
		var panel = new AttributeBasePanel(o);
		var dlg = new JDialog();
		dlg.add(panel);
		dlg.pack();
		invokeLater(new Runnable() {
			public void run() { dlg.setVisible(true); } });
	}
}
