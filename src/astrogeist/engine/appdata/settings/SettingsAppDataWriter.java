package astrogeist.engine.appdata.settings;

import java.io.OutputStream;

import aha.common.util.Cast;
import astrogeist.engine.appdata.AbstractNameValueXmlAppDataWriter;

public final class SettingsAppDataWriter
	extends AbstractNameValueXmlAppDataWriter  {

	public SettingsAppDataWriter() { super(Settings.class); }

	@Override public final void write(OutputStream out, Object data)
		throws Exception {
		
		var map = Cast.asOrThrow(Settings.class, data).asMap();
		super.write(out, map);
	}

}
