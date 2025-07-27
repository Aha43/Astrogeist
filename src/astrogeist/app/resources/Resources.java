package astrogeist.app.resources;

import java.net.URL;

public final class Resources {
	
	public static URL getLogoUrl(Object o) { return o.getClass().getResource("/astrogeist/app/resources/logo.png"); }

	private Resources() { throw new AssertionError("Can not instantiate static class"); }
}
