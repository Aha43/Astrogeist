package astrogeist.engine.appdata.runconfig;

import java.io.InputStream;

import aha.common.abstraction.io.appdata.AppDataReader;
import aha.common.io.appdata.AbstractAppData;
import aha.common.io.simpledataformat.SimpleDataFormatReader;

public final class RunConfigurationsAppDataReader extends AbstractAppData 
	implements AppDataReader {

	public RunConfigurationsAppDataReader() {
		super("txt", RunConfigurations.class); }

	@Override public final Object read(InputStream in) throws Exception {
		var data = SimpleDataFormatReader.parse(in);
		var retVal = new RunConfigurations();
		
		for (var e : data.entrySet()) {
			var rc = new DefaultRunConfiguration(e.getKey());
			retVal.addConfiguration(rc);
		}
		
		return retVal;
	}

	@Override public final Object createDefault() {
		return new RunConfigurations(); }

}
