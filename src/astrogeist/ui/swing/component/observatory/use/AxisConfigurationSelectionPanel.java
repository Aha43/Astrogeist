package astrogeist.ui.swing.component.observatory.use;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import aha.common.abstraction.IdNames;
import astrogeist.engine.observatory.Axis;
import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.ConfigurationMatcher;
import astrogeist.engine.observatory.DefaultConfigurationMatcher;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.observatory.Selection;

/**
 * <p>
 *   Panel that let user select configurations from all the
 *   {@link Axis} of an 
 *   {@link Observatory}.
 * </p>
 * <p>
 *   
 * </p>
 */
public final class AxisConfigurationSelectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final IdNames idNames;
	
	private final Map<String, ConfigurationSelectionPanel> selectionsPanels = 
		new HashMap<>();

	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param idNames              maps id to names for end user.
	 * @param observatory          {@link Observatory} to select
	 *                             {@link Configuration} from. 
	 * @param configurationMatcher {@link ConfigurationMatcher} to use. If 
	 *                             {@code null} uses
	 *                             {@link DefaultConfigurationMatcher}.
	 */
	public AxisConfigurationSelectionPanel(
		IdNames idNames,
		Observatory observatory, 
		ConfigurationMatcher matcher) {
		
		this.idNames = requireNonNull(idNames, "idNames");
		
		requireNonNull(observatory, "observatory");
		
		matcher = matcher == null ? 
			DefaultConfigurationMatcher.INSTANCE : matcher;
		
		super.setLayout(new BorderLayout());
		
		var tabs = new JTabbedPane();
		
		var axises = observatory.getAxises();
		for (var curr : axises) {
			var sp = new ConfigurationSelectionPanel(curr, matcher);
			tabs.add(curr.name(), sp);
			this.selectionsPanels.put(curr.id(), sp);
		}
		
		super.add(tabs, BorderLayout.CENTER);
	}
	
	public final void setSelection(Selection selection) {
		requireNonNull(selection, "selection");
		
		for (var id : selection.getAxisIds()) {
			var conf = selection.getConfigurationById(id);
			var panel = this.selectionsPanels.get(id);
			panel.setSelected(conf);
		}
	}
	
	public final Selection getSelection() {
		var retVal = new Selection(this.idNames);
		
		for (var curr : this.selectionsPanels.values()) {
			var conf = curr.getSelectedConfiguration();
			if (conf != null) retVal.add(conf);
		}
		
		return retVal;
	}
	
}
