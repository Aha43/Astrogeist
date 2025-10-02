package astrogeist.engine.integration.api.astrometry.model;

public abstract class AstrometricModel {

	private final static String ls = System.lineSeparator();
	
	protected final static StringBuilder appendNameValue(String name, String val, StringBuilder sb) {
		return sb.append(name).append(" = '").append(val).append("'").append(ls); }
	protected final static StringBuilder appendNameValue(String name, int val, StringBuilder sb) {
		return sb.append(name).append(" = '").append(val).append("'").append(ls); }
	protected final static StringBuilder appendNameValue(String name, double val, StringBuilder sb) {
		return sb.append(name).append(" = '").append(val).append("'").append(ls); }
	protected final static StringBuilder appendNameValues(String name, String[] values, StringBuilder sb) {
		return sb.append(name).append(" = '").append(String.join(",", values)).append("'").append(ls); }
}
