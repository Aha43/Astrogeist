package astrogeist.engine.observatory.edit;

import astrogeist.engine.observatory.Item;

public interface ConfigurationEditStep {
	EditAction action();
	Item item();
}
