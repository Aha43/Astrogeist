package astrogeist.ui.swing.component.observatory1;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import astrogeist.engine.observatory1.Configuration;
import astrogeist.engine.observatory1.Observatory;
import astrogeist.ui.swing.App;

public final class SelectConfigurationDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final ConfigurationsPanel configurationsPanel =
		new ConfigurationsPanel();
	
	private Configuration selected = null;
	
	private final JButton okButton = new JButton("OK");
	
	private SelectConfigurationDialog(App app, String configuration) {
		super(app.getFrame());
		super.setModal(true);
		super.setTitle("Select Configuration");
		super.add(this.configurationsPanel, BorderLayout.CENTER);
		
		var observatory = app.service(Observatory.class);
		
		this.configurationsPanel.data(observatory, configuration);
		
		this.setChangeListener();
		
		var south = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		this.okButton.setEnabled(false);
		this.okButton.addActionListener(e -> dispose());
		south.add(this.okButton);
		
		var cancel = new JButton("Cancel");
		cancel.addActionListener(e -> { selected = null; dispose(); });
		south.add(cancel);
		
		super.add(south, BorderLayout.SOUTH);
		
		super.pack();
		
		super.setSize(500, 500);
	}
	
	// Part of construction.
	private final void setChangeListener() {
		this.configurationsPanel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) { setSelected(); }
		});
	}
	
	private final void setSelected() { 
		this.selected = this.configurationsPanel.selected();
		this.okButton.setEnabled(this.selected != null);
	}
	
	public final static Configuration show(App app, String current) {
		var dlg =
			new SelectConfigurationDialog(requireNonNull(app, "app"), current);
		dlg.setVisible(true);
		return dlg.selected;
	}
}
