package astrogeist.engine.abstraction.persistence;

public interface AstrogeistStorageManager {
	<T> T load(Class<T> type) throws Exception;
	<T> void save(Class<T> type, T data) throws Exception;
}
