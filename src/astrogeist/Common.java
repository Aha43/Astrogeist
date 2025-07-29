package astrogeist;

public final class Common {
	
	public static String requireNonEmpty(String value, String name) {
	    if (value == null || value.isEmpty()) {
	        throw new IllegalArgumentException(name + " must not be null or empty");
	    }
	    return value;
	}
	
	public static void throwStaticClassInstantiateError() { throw new AssertionError("Can not instantiate static class"); }
	
	private Common() { throwStaticClassInstantiateError(); }
}
