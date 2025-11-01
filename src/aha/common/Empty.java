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
	char[] charArray = new char[0];
	Character[] CharacterArray = new Character[0];
	short[] shortArray = new short[0];
	String[] StringArray = new String[0];
}
