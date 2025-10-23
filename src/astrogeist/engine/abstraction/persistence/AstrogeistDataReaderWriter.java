package astrogeist.engine.abstraction.persistence;

import java.io.InputStream;
import java.io.OutputStream;

public interface AstrogeistDataReaderWriter {
	String key();
	Object read(InputStream in) throws Exception;
	void write(OutputStream out, Object data) throws Exception;
}
