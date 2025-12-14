package aha.common.guard;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map; 

/**
 * <p>
 *   Methods to check for some collection arguments requirements.
 * </p>
 */
public final class CollectionGuards {
	private CollectionGuards() { throwStaticClassInstantiateError(); }
	
	/**
	 * <p>
	 *   Requires that the collection is not empty.
	 * </p>
	 * @param value the collection.
	 * @param name  the argument name to be mentioned in any error messages.
	 * @return {@code value}.
	 * @throws NullPointerException if {@code value == null}.
	 * @throws IllegalArgumentException if {@code value.isEmpty()}.
	 */
	public static <T, C extends Collection<T>> C requireNotEmpty(C value,
		String name) {
		
		requireNonNull(value, name);
		
		if (value.isEmpty()) throw new IllegalArgumentException(name +
			" is empty");
		return value;
	}
	
	public static <T> T[] requireNotEmpty(T[] value, String name) {
		requireNonNull(value, name);
		
	    if (value.length == 0)
	        throw new IllegalArgumentException(name + " is empty");
	    return value;
	}
	
	/**
	 * <p>
     *   Requires that the collection itself is non-null and that all elements 
     *   are non-null.
     * </p>
     * 
     * @return the {@code value}.
     * @throws NullPointerException If {@code value} is {@code null} or contain
     *         an element that is {@code null}.
     */
	public static <T, C extends Collection<T>> C requireAllNonNull(C value,
		String name) {
	    
		requireNonNull(value, name);
		
	    int i = 0;
	    for (T element : value) {
	        if (element == null)
	            throw new NullPointerException(name + "[" + i + "] is null");
	        i++;
	    }
	    return value;
	}
	
	/**
	 * <p>
     *   Requires that the array itself is non-null and that all elements are
     *   non-null.
     * </p>
     * @return the {@code value}.
     * @throws NullPointerException If {@code value} is {@code null} or contain
     *         an element that is {@code null}. 
     */
	public static <T> T[] requireAllNonNull(T[] value, String name) {
        requireNonNull(value, name);
        for (int i = 0; i < value.length; i++) {
            if (value[i] == null)
                throw new NullPointerException(name + "[" + i + "] is null");
        }
        return value;
    }
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if map does not have given key.
	 * </p>
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * * @param key the key.
	 * @param map the map.
	 * @return the {@code key}.
	 */
	public static <K, V> K requireKeyExists(K key, Map<K, V> map){
		return requireKeyExists(key, map, null); }
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if map does not have given key.
	 * </p>
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * @param key the key. 
	 * @param map the map.
	 * @param name the name used in exception to refer to {@code key} 
	 *             (typically a method parameter name).
	 * @return the {@code map}.
	 */
	public static <K, V> K requireKeyExists(K key, Map<K, V> map, String name) {
		
		if (map.containsKey(key)) return key;
		name = (name == null) ? "key" : name;
		throw new IllegalArgumentException(name + " not found");
	}
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if map does have given key.
	 * </p>
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * @param key the key. 
	 * @param map the map.
	 * @param name the name used in exception to refer to {@code key} 
	 *             (typically a method parameter name).
	 * @return the {@code key}.
	 */
	public static <K, V> K requireKeyNotExists(K key, Map<K, V> map) {
		return requireKeyNotExists(key, map, null); }
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if map does have given key.
	 * </p>
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * @param key the key.
	 * @param map the map.
	 * @param name the name used in exception to refer to {@code key} 
	 *             (typically a method parameter name).
	 * @return the {@code key}.
	 */
	public static <K, V> K requireKeyNotExists(K key, Map<K, V> map, 
		String name) {
		
		if (!map.containsKey(key)) return key;
		name = (name == null) ? "key" : name;
		throw new IllegalArgumentException(name + " exists");
	}
	
}
