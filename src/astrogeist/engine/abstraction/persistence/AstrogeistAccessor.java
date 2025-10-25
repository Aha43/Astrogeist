package astrogeist.engine.abstraction.persistence;

public interface AstrogeistAccessor {
	Object load(AstrogeistDataReader reader) throws Exception;
	void save(AstrogeistDataWriter writer, Object data) throws Exception;
}
