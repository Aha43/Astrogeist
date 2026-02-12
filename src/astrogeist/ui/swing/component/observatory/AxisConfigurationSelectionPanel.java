package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import astrogeist.engine.observatory.ConfigurationMatcher;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.observatory.Setup;

public final class AxisConfigurationSelectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final Map<String, ConfigurationSelectionPanel> selectionsPanels = 
		new HashMap<>();

	public AxisConfigurationSelectionPanel(Observatory observatory, 
			ConfigurationMatcher configurationMatcher) {
		
		requireNonNull(observatory, "observatory");
		requireNonNull(configurationMatcher, "configurationMatcher");
		
		super.setLayout(new BorderLayout());
		
		var tabs = new JTabbedPane();
		
		var axises = observatory.getAxises();
		for (var curr : axises) {
			var sp = new ConfigurationSelectionPanel(curr,
				configurationMatcher);
			var name = curr.name();
			tabs.add(name, sp);
			this.selectionsPanels.put(name, sp);
		}
		
		super.add(tabs, BorderLayout.CENTER);
	}
	
	public final Setup getSetup() {
		var retVal = new Setup();
		
		for (var curr : this.selectionsPanels.values()) {
			var conf = curr.getSelectedConfiguration();
			if (conf != null) retVal.add(conf);
		}
		
		return retVal;
	}
	
}
