package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import astrogeist.engine.observatory.AhaObservatory;
import astrogeist.engine.observatory.ConfigurationMatcher;
import astrogeist.engine.observatory.DefaultConfigurationMatcher;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.observatory.Setup;
import astrogeist.ui.swing.App;

public final class AxisConfigurationSelectionDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private AxisConfigurationSelectionPanel axisConfigurationSelectionPanel;
	
	private Setup selected = new Setup();
	
	public AxisConfigurationSelectionDialog(App app) {
		super(requireNonNull(app, "app").getFrame());
		buildUi(app.service(Observatory.class), null); 
	}
	
	public AxisConfigurationSelectionDialog(Observatory observatory) {	
		buildUi(observatory, null); }
	
	private void buildUi(Observatory observatory,
		ConfigurationMatcher matcher) {
		
		requireNonNull(observatory, "observatory");
		
		super.setModal(true);
		super.setTitle("Configurations");
		
		matcher = matcher == null ? 
			DefaultConfigurationMatcher.INSTANCE : matcher;
		
		this.axisConfigurationSelectionPanel = 
			new AxisConfigurationSelectionPanel(observatory,
				DefaultConfigurationMatcher.INSTANCE);
		super.add(this.axisConfigurationSelectionPanel, BorderLayout.CENTER);
			
		var buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var ok = new JButton("OK");
		buttons.add(ok);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = axisConfigurationSelectionPanel.getSetup();
				setVisible(false);
			} 
		});
		super.add(buttons, BorderLayout.SOUTH);
			
		super.pack();
	}
	
	public final Setup selected() { return new Setup(this.selected); }
	
	public final static Setup showDialog(Observatory observatory) {
		var dlg = new AxisConfigurationSelectionDialog(observatory);
		return showDialog(dlg);
	}
	
	public final static Setup showDialog(App app) {
		var dlg = new AxisConfigurationSelectionDialog(app);
		return showDialog(dlg);
	}
	
	private static Setup showDialog(AxisConfigurationSelectionDialog dlg) {
		dlg.setVisible(true);
		return dlg.selected();
	}
	
	public static void main(String[] args) {
		var setup = showDialog(new AhaObservatory());
	}

}
