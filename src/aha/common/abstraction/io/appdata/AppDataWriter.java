package aha.common.abstraction.io.appdata;

import java.io.OutputStream;

public interface AppDataWriter extends AppData {
	void write(OutputStream out, Object data) throws Exception;
}
