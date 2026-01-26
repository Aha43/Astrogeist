package astrogeist.ui.swing.component.observatory1;

import static aha.common.util.Cast.as;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import astrogeist.engine.observatory1.Configuration;
import astrogeist.engine.observatory1.Instrument;
import astrogeist.engine.observatory1.ObservatorySystem;

final class ConfigurationTreeCellRenderer 
	extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public static TreeCellRenderer INSTANCE =
		new ConfigurationTreeCellRenderer();
	
	private ConfigurationTreeCellRenderer() {}

	@Override public Component getTreeCellRendererComponent(JTree tree, 
		Object value, boolean selected, boolean expanded, boolean leaf, int row, 
			boolean hasFocus) {
		return super.getTreeCellRendererComponent(tree, getValue(value), 
			selected, expanded, leaf, row, hasFocus);
	}
	
	private static Object getValue(Object value) {
		var configuration = as(Configuration.class, value);
		if (configuration != null) return configuration.code();
		
		var system = as(ObservatorySystem.class, value);
		if (system != null) return system.displayName();
		
		var instrument = as(Instrument.class, value);
		if (instrument != null) return instrument.name();
		
		return value;
	}

}
