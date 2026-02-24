package astrogeist.ui.swing.component.observatory.use;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import aha.common.abstraction.IdNames;
import aha.common.util.DefaultIdNames;
import astrogeist.engine.observatory.AhaObservatory;
import astrogeist.engine.observatory.ConfigurationMatcher;
import astrogeist.engine.observatory.DefaultConfigurationMatcher;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.observatory.Selection;
import astrogeist.ui.swing.App;

public final class AxisConfigurationSelectionDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private IdNames idNames;
	
	private AxisConfigurationSelectionPanel axisConfigurationSelectionPanel;
	
	private Selection selected = null;
	
	public AxisConfigurationSelectionDialog(App app) {
		super(requireNonNull(app, "app").getFrame()); this.init(app); }
	
	public AxisConfigurationSelectionDialog(App app, Selection selected) {
		super(requireNonNull(app, "app").getFrame());
		this.selected = requireNonNull(selected, "selected");
		this.init(app);
		this.axisConfigurationSelectionPanel.setSelection(selected);
	}
	
	private void init(App app) {
		this.idNames = app.service(IdNames.class);
		this.buildUi(app.service(Observatory.class), null);
	}
	
	
	
	private void buildUi(Observatory observatory,
		ConfigurationMatcher matcher) {
		
		requireNonNull(observatory, "observatory");
		
		super.setModal(true);
		super.setTitle("Configurations");
		
		matcher = matcher == null ? 
			DefaultConfigurationMatcher.INSTANCE : matcher;
		
		this.axisConfigurationSelectionPanel = 
			new AxisConfigurationSelectionPanel(this.idNames, observatory,
				DefaultConfigurationMatcher.INSTANCE);
		super.add(this.axisConfigurationSelectionPanel, BorderLayout.CENTER);
			
		var buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var ok = new JButton("OK");
		buttons.add(ok);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = axisConfigurationSelectionPanel.getSelection();
				setVisible(false);
			} 
		});
		super.add(buttons, BorderLayout.SOUTH);
			
		super.pack();
	}
	
	public Selection showDialog() { 
		this.setVisible(true); return this.selected; }
	
//	public final static Selection showDialog(App app, Selection selection) {
//		requireNonNull(app, "app");
//		requireNonNull(selection, "selection");
//		var dlg = new AxisConfigurationSelectionDialog(app, selection);
//		return showDialog(dlg);
//	}
//	
//	public final static Selection showDialog(App app) {
//		requireNonNull(app, "app");
//		var dlg = new AxisConfigurationSelectionDialog(app);
//		return showDialog(dlg);
//	}
	
//	private static Selection showDialog(AxisConfigurationSelectionDialog dlg) {
//		dlg.setVisible(true);
//		return dlg.selected;
//	}
	
	// -- Demo / test
	
	/**
	 * <p>
	 *   Constructor used only by demo / test main.
	 * </p>
	 * @param observatory {@link Observatory} that owns axis to select from. 
	 * @param idNames     the map from id to names for the end user.
	 */
	private AxisConfigurationSelectionDialog(IdNames idNames, 
		Observatory observatory) {	
			
		this.idNames = requireNonNull(idNames, "idNames");
		this.buildUi(observatory, null);
	}
	
	/**
	 * <p>
	 *   Show method for demo / test main only.
	 * </p>
	 * @param observatory {@link Observatory} that owns axis to select from. 
	 * @param idNames     the map from id to names for the end user. 
	 * @return            {@link Selection} done.
	 */
	private final static Selection showDialog(Observatory observatory, 
		IdNames idNames) {
			
		var dlg = new AxisConfigurationSelectionDialog(idNames, observatory);
		return dlg.showDialog();
	}
	
	/**
	 * <p>
	 *   Demo / test program.
	 * </p>
	 * @param args Command line arguments not used.
	 */
	public static void main(String[] args) {
		var idNames = new DefaultIdNames();
		var selection = showDialog(new AhaObservatory(idNames), idNames);
		System.out.println(selection);
	}

}
