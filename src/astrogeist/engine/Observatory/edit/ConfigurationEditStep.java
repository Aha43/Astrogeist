package astrogeist.engine.Observatory.edit;

import astrogeist.engine.Observatory.Item;

public interface ConfigurationEditStep {
	EditAction action();
	Item item();
}
