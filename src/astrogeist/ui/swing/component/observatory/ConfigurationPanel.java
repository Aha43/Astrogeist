package astrogeist.ui.swing.component.observatory;

import static javax.swing.SwingUtilities.invokeLater;
import static aha.common.util.Cast.as;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import aha.common.ui.swing.panels.AttributeBasePanel;
import astrogeist.engine.observatory.Instrument;
import astrogeist.engine.observatory.Observatory;

public final class ConfigurationPanel extends JPanel 
	implements TreeSelectionListener {
	
	private static final long serialVersionUID = 1L;
	
	private ConfigurationTreePanel configurationTreePanel = 
		new ConfigurationTreePanel();
	private AttributeBasePanel attributePanel = new AttributeBasePanel();
	
	public ConfigurationPanel() {
		super.setLayout(new BorderLayout());
		var sp = new JSplitPane();
		sp.setLeftComponent(this.configurationTreePanel);
		sp.setRightComponent(this.attributePanel);
		this.configurationTreePanel.addSelectionListener(this);
		add(sp, BorderLayout.CENTER);
	}
	
	public void data(Observatory observatory) {
		this.configurationTreePanel.data(observatory);
	}
	
	@Override public void valueChanged(TreeSelectionEvent e) {
		this.attributePanel.clear();
		var s = e.getPath();
		var o = s.getLastPathComponent();
		var instrument = as(Instrument.class, o);
		if (instrument != null) this.attributePanel.data(instrument);
		System.out.println(o);
	}
	
	public static void showDialog(Observatory observatory) {
		var dlg = new JDialog();
		var panel = new ConfigurationPanel();
		panel.data(observatory);
		dlg.add(panel);
		dlg.pack();
		dlg.setSize(500, 500);
		invokeLater(new Runnable() {
			@Override public void run() { dlg.setVisible(true); } });
	}

}
