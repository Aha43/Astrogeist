package aha.common;

/**
 * <p>
 *   Place to put shared immutable empty instances of common types. 
 * </p>
 * <p>
 *   Shared immutable empty instances of any implemented types should
 *   be put in the interface, record or class declaration.
 * </p>
 */
public interface Empty {
	boolean[]   booleanArray   = new boolean[0];
	Boolean[]   BooleanArray   = new Boolean[0];
	char[]      charArray      = new char[0];
	Character[] CharacterArray = new Character[0];
	short[]     shortArray     = new short[0];
	Short[]     ShortArray     = new Short[0];
	int[]       intArray       = new int[0];
	Integer[]   IntegerArray   = new Integer[0];
	long[]      longArray      = new long[0];
	Long[]      LongArray      = new Long[0];
	float[]     floatArray     = new float[0];
	Float[]     FloatArray     = new Float[0];
	double[]    doubleArray    = new double[0];
	Double[]    DoubleArray    = new Double[0];
	String[]    StringArray    = new String[0];
	Object[]    ObjectArray    = new Object[0];
}
