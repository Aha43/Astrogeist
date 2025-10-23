package astrogeist.engine.persitence.disk.scannerconfig;

import java.io.InputStream;
import java.io.OutputStream;

import astrogeist.common.exceptions.NotImplementedException;
import astrogeist.engine.abstraction.persistence.AstrogeistDataReaderWriter;


public class ScannerConfigAstrogeistDataReaderWriter implements AstrogeistDataReaderWriter  {

	@Override public String key() { return "scanner-config"; }

	@Override public Object read(InputStream in) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override public void write(OutputStream out, Object data) throws Exception {
		throw new NotImplementedException(); }

}
