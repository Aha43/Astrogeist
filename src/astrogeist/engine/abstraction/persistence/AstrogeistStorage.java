package astrogeist.engine.abstraction.persistence;

public interface AstrogeistStorage {
	void configure(String key, AstrogeistDataHandler astrogeistData,
		AstrogeistDataReaderWriter astrogeistDataReaderWriter);
	Object load(String type) throws Exception;
	void save(String type, Object data) throws Exception;
}
