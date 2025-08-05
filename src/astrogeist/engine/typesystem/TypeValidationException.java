package astrogeist.engine.typesystem;

public class TypeValidationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public TypeValidationException(Type type, Object v, String msg) { 
		super(type.getName() + " (" + v.toString() + ") " + msg); }
}
