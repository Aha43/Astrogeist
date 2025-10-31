package astrogeist.engine.abstraction.persistence;

public interface AstrogeistStorageManager {
	<T> T load(Class<T> type) throws Exception;
	void save(Object data) throws Exception;
}
