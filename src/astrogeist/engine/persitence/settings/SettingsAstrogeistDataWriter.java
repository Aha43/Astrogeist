package astrogeist.engine.persitence.settings;

import java.io.OutputStream;

import aha.common.Cast;
import astrogeist.engine.persitence.AbstractNameValueXmlAstrogeistDataWriter;

public final class SettingsAstrogeistDataWriter extends AbstractNameValueXmlAstrogeistDataWriter  {

	public SettingsAstrogeistDataWriter() { super(Settings.class); }

	@Override public final void write(OutputStream out, Object data) throws Exception {
		var map = Cast.asOrThrow(Settings.class, data).data();
		super.write(out, map);
	}

}
