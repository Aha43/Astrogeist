package aha.common.guard;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.guard.StringGuards.requireNonEmpty;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;

import aha.common.exceptions.runtime.DuplicateException;
import aha.common.util.NamedList;
import aha.common.util.Safe; 

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
		
		if (value.isEmpty()) throw new IllegalArgumentException(
			Safe.string(name) + " is empty");
		return value;
	}
	
	/**
	 * <p>
	 *   Requires that the array is not empty.
	 * </p>
	 * @param value the array.
	 * @param name  the argument name to be mentioned in any error messages.
	 * @return {@code value}.
	 * @throws NullPointerException if {@code value == null}.
	 * @throws IllegalArgumentException if {@code value.isEmpty()}.
	 */
	public static <T> T[] requireNotEmpty(T[] value, String name) {
		requireNonNull(value, name);
		
	    if (value.length == 0)
	        throw new IllegalArgumentException(Safe.string(name) + " is empty");
	    return value;
	}
	
	/**
	 * <p>
     *   Requires that the collection itself is non-null and that all elements 
     *   are non-null.
     * </p>
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
	            throw new NullPointerException(Safe.string(name) + 
	            	"[" + i + "] is null");
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
                throw new NullPointerException(Safe.string(name)
                	+ "[" + i + "] is null");
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
	 * @param key the key.
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
	 * @param <K>  the key type.
	 * @param <V>  the value type.
	 * @param key  the key. 
	 * @param map  the map.
	 * @param name the name used in exception to refer to {@code key} 
	 *             (typically a method parameter name).
	 * @return the {@code key}.
	 */
	public static <K, V> K requireKeyExists(K key, Map<K, V> map, String name) {
		requireNonNull(key, name);
		
		if (map.containsKey(key)) return key;
		throw new NoSuchElementException(Safe.string(name, "key"));
	}
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if
	 *   {@link NamedList} does not have given key.
	 * </p>
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * @param key the key. 
	 * @param map the map.
	 * @return the {@code key}.
	 */
	public static <V> String requireKeyExists(String key, NamedList<V> map) {
		return requireKeyExists(key, map, null); }
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if
	 *   {@link NamedList} does not have given key.
	 * </p>
	 * @param <K>  the key type.
	 * @param <V>  the value type.
	 * @param key  the key. 
	 * @param map  the map.
	 * @param name the name used in exception to refer to {@code key} 
	 *             (typically a method parameter name).
	 * @return the {@code key}.
	 */
	public static <V> String requireKeyExists(String key, NamedList<V> map, 
		String name) {
		
		requireNonEmpty(key, "key");
		if (map.containsKey(key)) return key;
		throw new NoSuchElementException(Safe.string(name, "key"));
	}
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if map does have given key.
	 * </p>
	 * @param <K>  the key type.
	 * @param <V>  the value type.
	 * @param key  the key. 
	 * @param map  the map.
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
	 * @param <K>  the key type.
	 * @param <V>  the value type.
	 * @param key  the key.
	 * @param map  the map.
	 * @param name the name used in exception to refer to {@code key} 
	 *             (typically a method parameter name).
	 * @return the {@code key}.
	 */
	public static <K, V> K requireKeyNotExists(K key, Map<K, V> map, 
		String name) {
		
		requireNonNull(key, name);
		if (!map.containsKey(key)) return key;
		throw new DuplicateException(Safe.string(name, "key"));
	}
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if
	 *   {@link NamedList} does have given key.
	 * </p>
	 * @param <V> the value type.
	 * @param key the key.
	 * @param map the map.
	 * @return the {@code key}.
	 */
	public static <V> String requireKeyNotExists(String key, NamedList<V> map) {
		return requireKeyNotExists(key, map, null); }
	
	/**
	 * <p> 
	 *   Throws
	 *   {@link IllegalArgumentException} if
	 *   {@link NamedList} does have given key.
	 * </p>
	 * @param <V>  the value type.
	 * @param key  the key.
	 * @param map  the map.
	 * @param name the name used in exception to refer to {@code key} 
	 *             (typically a method parameter name).
	 * @return the {@code key}.
	 */
	public static <V> String requireKeyNotExists(String key, NamedList<V> map, 
		String name) {
		
		requireNonEmpty(key, "key");
		if (!map.containsKey(key)) return key;
		throw new DuplicateException(Safe.string(name, "key"));
	}
	
	/**
	 * <p>
	 *   Throws
	 *   {@link NoSuchElementException} if collection does not contain
	 *   {@code e}.
	 * </p>
	 * @param <E>  the element type.
	 * @param e    the required element.
	 * @param c    the collection where {@code e} must be in.
	 * @param name the name used in exception to refer to {@code e} (typically a
	 *             method parameter name). 
	 * @return {@code e}.
	 */
	public static <E> E requireContains(E e, Collection<E> c, String name) {
		if (c.contains(requireNonNull(e, name))) return e;
		throw new NoSuchElementException(name);
	}
	
	/**
	 * <p>
	 *   Throws
	 *   {@link NoSuchElementException} if collection does not contain
	 *   {@code e}.
	 * </p>
	 * @param <E> the element type.
	 * @param e   the required element.
	 * @param c   the collection where {@code e} must be in. 
	 * @return {@code e}.
	 */
	public static <E> E requireContains(E e, Collection<E> c) {
		if (c.contains(requireNonNull(e))) return e;
		throw new NoSuchElementException();
	}
	
	/**
	 * <p>
	 *   Throws
	 *   {@link DuplicateException} if collection does contain {@code e}.
	 * </p>
	 * @param <E>  the element type.
	 * @param e    the element collection must not contain.
	 * @param c    the collection where {@code e} must be in.
	 * @param name the name used in exception to refer to {@code e} (typically a
	 *             method parameter name). 
	 * @return {@code e}.
	 */
	public static <E> E requireNoDuplicate(E e, Collection<E> c) {
		if (!c.contains(requireNonNull(e))) return e;
		throw new DuplicateException();
	}
	
	/**
	 * <p>
	 *   Throws
	 *   {@link DuplicateException} if collection does contain {@code e}.
	 * </p>
	 * @param <E> the element type.
	 * @param e   the element collection must not contain.
	 * @param c   the collection where {@code e} must be in. 
	 * @return {@code e}.
	 */
	public static <E> E requireNoDuplicate(E e, Collection<E> c, String name) {
		if (!c.contains(requireNonNull(e, name))) return e;
		throw new DuplicateException(name);
	}
	
}
