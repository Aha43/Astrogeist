package astrogeist.engine.setting;

import astrogeist.common.Common;

public final class SettingKeys {
	private SettingKeys() { Common.throwStaticClassInstantiateError(); }
	
	public static final String DATA_ROOTS="scanner:roots";
	public static final String TABLE_COLUMNS="ui:table-columns";
}
