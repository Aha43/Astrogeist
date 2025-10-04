package astrogeist.engine.setting;

import astrogeist.common.Guards;

public final class SettingKeys {
	private SettingKeys() { Guards.throwStaticClassInstantiateError(); }
	
	public static final String DATA_ROOTS="scanner:roots";
	public static final String TABLE_COLUMNS="ui:table-columns";
}
