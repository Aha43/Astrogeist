package astrogeist.engine.abstraction.persistence;

import java.io.OutputStream;

public interface AstrogeistDataWriter extends AstrogeistData {
	void write(OutputStream out, Object data) throws Exception;
}
