package astrogeist.ui.swing.component.observatory;

import static aha.common.util.Cast.as;
import static java.util.Objects.requireNonNull;

import aha.common.ui.swing.tree.AbstractTreeModel;
import astrogeist.engine.observatory.Configuration;
import astrogeist.engine.observatory.Configurations;
import astrogeist.engine.observatory.Instrument;
import astrogeist.engine.observatory.Observatory;
import astrogeist.engine.observatory.ObservatorySystem;

final class ConfigurationsTreeModel extends AbstractTreeModel {

	private final Configurations configurations;
	
	public ConfigurationsTreeModel(Observatory observatory) {
		super(requireNonNull(observatory, "observatory"));
		this.configurations = observatory.configurations();
	}
	
	public Configuration getByCode(String code) {
		return this.configurations.getByCode(requireNonNull(code, "code")); }

	@Override public final Object getChild(Object parent, int idx) {
		if (parent == super.getRoot()) return this.configurations.get(idx);
		
		var configuration = as(Configuration.class, parent);
		if (configuration != null) return configuration.getByIndex(idx);
		
		var system = as(ObservatorySystem.class, parent);
		if (system != null) return system.get(idx);
		
		return null;
	}

	@Override public final int getChildCount(Object parent) {
		if (parent == getRoot()) return this.configurations.size();
		
		var configuration = as(Configuration.class, parent);
		if (configuration != null) return configuration.size();
		
		var system = as(ObservatorySystem.class, parent);
		if (system != null) return system.size();
		
		return 0;
	}

	@Override public final boolean isLeaf(Object node) {
		if (node == getRoot()) return false;
		
		var configuration = as(Configuration.class, node);
		if (configuration != null) return false;
		
		var system = as(ObservatorySystem.class, node);
		if (system != null) return false;
		
		return true;
	}

	@Override public final int getIndexOfChild(Object parent, Object child) {
		if (parent == getRoot()) {
			var configuration = as(Configuration.class, child);
			if (configuration != null) 
				return this.configurations.indexOf(configuration); 
		}
		
		var configuration = as(Configuration.class, parent);
		if (configuration != null) {
			var system = as(ObservatorySystem.class, child);
			if (system != null) return configuration.indexOf(system);
		}
		
		var system = as(ObservatorySystem.class, parent);
		if (system != null) {
			var instrument = as(Instrument.class, child);
			if (instrument != null) return system.indexOf(instrument);
		}
		
		return -1;
	}

}
