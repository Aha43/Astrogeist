package astrogeist.engine.abstraction.persistence;

import java.io.InputStream;

public interface AstrogeistDataReader extends AstrogeistData {
	Object read(InputStream in) throws Exception;
	Object createDefault();
}
