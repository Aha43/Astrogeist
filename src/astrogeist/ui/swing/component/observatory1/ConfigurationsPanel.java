package astrogeist.ui.swing.component.observatory1;

import static aha.common.util.Cast.as;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import aha.common.ui.swing.panels.AttributeBasePanel;
import aha.common.util.AttributeBase;
import astrogeist.engine.observatory1.Configuration;
import astrogeist.engine.observatory1.Observatory;

/**
 * <p>
 *   Panel that shows
 *   {@link Configuration}s using 
 *   {@link ConfigurationsTreePanel} and
 *   {@link AttributeBasePanel}.
 * </p>
 */
public final class ConfigurationsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private ConfigurationsTreePanel configurationTreePanel = 
		new ConfigurationsTreePanel();
	private AttributeBasePanel attributePanel = new AttributeBasePanel();
	
	private final ChangeEvent changeEvent = new ChangeEvent(this);
	
	private final List<ChangeListener> listeners = new ArrayList<>();
	
	public final void addChangeListener(ChangeListener l) {
		this.listeners.add(l); }
	public final void removeChangeListener(ChangeListener l) {
		this.listeners.remove(l); }
	
	private final void fireChangeEvent() { 
		for (var l : this.listeners) l.stateChanged(this.changeEvent); }
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 */
	public ConfigurationsPanel() {
		super.setLayout(new BorderLayout());
		var sp = new JSplitPane();
		sp.setLeftComponent(this.configurationTreePanel);
		sp.setRightComponent(this.attributePanel);
		this.setTreeSelectionListent();
		add(sp, BorderLayout.CENTER);
	}
	
	// Part of construction.
	private final void setTreeSelectionListent() {
		this.configurationTreePanel.addTreeSelectionListener(
			new TreeSelectionListener() {
			
			public void valueChanged(TreeSelectionEvent e) { 
				handleSelection(e); }
		});
	}
	
	private final void handleSelection(TreeSelectionEvent e) {
		attributePanel.clear();
		var s = e.getPath();
		var o = s.getLastPathComponent();
		var attributes = as(AttributeBase.class, o);
		if (attributes != null) attributePanel.data(attributes);
		fireChangeEvent();
	}
	
	/**
	 * <p>
	 *   Sets the
	 *   {@link Observatory} which
	 *   {@link Configuration}s to show.
	 * </p>
	 * @param observatory 	the {@link Observatory}.
	 * @param configuration names the 
	 *                      {@link Configuration} (code) to show as selected, 
	 *        				{@code null} if none are to be shown as selected.
	 */
	public void data(Observatory observatory, String configuration) {
		this.configurationTreePanel.data(observatory, configuration); }
	
	public final Configuration selected() { 
		return this.configurationTreePanel.selected(); }
	
	/**
	 * <p>
	 *   Shows dialog with the panel and a close button.
	 * </p>
	 * @param observatory Content.
	 */
	public static void showDialog(Frame parent, Observatory observatory,
		String configuration) {
		
		var dlg = new JDialog(parent, "Observatory Configurations");
		var panel = new ConfigurationsPanel();
		panel.data(observatory, configuration);
		dlg.add(panel);
		var south = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var closeButton = new JButton("Close");
		closeButton.addActionListener(e -> dlg.setVisible(false));
		south.add(closeButton);
		dlg.add(south, BorderLayout.SOUTH);
		dlg.pack();
		dlg.setSize(500, 500);
		invokeLater(new Runnable() {
			@Override public void run() { dlg.setVisible(true); } });
	}

}
