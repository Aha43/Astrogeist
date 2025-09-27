package astrogeist.engine.abstraction;

public interface ServiceProvider {
	<T> T get(Class<? extends T> clazz);
}
