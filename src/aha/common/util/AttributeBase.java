package aha.common.util;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Cast.as;
import static aha.common.util.Strings.quote;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Short.parseShort;
import static java.util.Collections.synchronizedMap;
import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import aha.common.abstraction.Named;
import aha.common.exceptions.runtime.ReadOnlyException;

/**
 * <p>
 *   AttributeBase is a strongly typed attribute map with the following
 *   rules:
 * </p>
 * <ol>
 *   <li>
 *     Null values are not allowed.
 *   </li>
 *   <li>
 *     get(name) throws if attribute is missing.
 *   </li>
 *   <li>
 *     get(name, defaultValue) returns defaultValue if missing.
 *   </li>
 *   <li>
 *     getAsX(name) throws if missing.
 *   </li>
 *   <li>
 *     getAsX(name, default) returns default if missing.
 *   </li>
 * </ol>
 * <p>
 *   Call exists(name) to test for attribute presence.
 * </p>
 * <p>
 *   See
 *   {@link AttributeObject} if to just instantiate a 'type safe map'.
 * </p>
 * @see AttributeObject
 */
public abstract class AttributeBase<T extends AttributeBase<T>> {	
	@SuppressWarnings("unchecked")
	private final T self() { return (T) this; }
	
	private final Map<String, Object> data = 
		synchronizedMap(new LinkedHashMap<>());
	
	private boolean readonly = false;
	
	/**
	 * <p>
	 *   Creates an empty attribute object with no initial entries.
	 * </p>
	 */
	protected AttributeBase() {}
	
	/**
	 * <p>
	 *   Copy constructor.
	 * </p>
	 * @param o Other to copy.
	 */
	protected AttributeBase(AttributeBase<T> o) {
		this.data.putAll(requireNonNull(o, "o").data); }
	
	/**
	 * <p>
	 *   Creates an attribute object initialized from a map of string
	 *   attributes.
	 * </p>
	 * <p>
	 *   Each entry in {@code data} is copied into this object using the same 
	 *   key, and the associated value is stored as a {@link String}. The map
	 *   must not contain {@code null} keys or values, and keys must be 
	 *   non-empty.
	 * </p>
	 * @param data the initial key–value pairs to populate this object with
	 * @throws NullPointerException if {@code data} or any value in it is
	 *         {@code null}
	 * @throws IllegalArgumentException if a key in {@code data} is empty
	 */
	protected AttributeBase(Map<String, String> data) {
		this();
	    for (var entry : requireNonNull(data, "data").entrySet())
	        this.data.put(requireNonEmpty(entry.getKey(), "key"), 
	        	requireNonNull(entry.getValue(), "value"));
	}
	
	/**
	 * <p>
	 *   Makes this read only.
	 * </p>
	 * <p>
	 *   One way, once made read only can not change to read / write.
	 * </p>
	 * @return {@code this}.
	 */
	public final T seal() { this.readonly = true; return self(); }
	
	/**
	 * <p>
	 *   Tells if {@code this} is read only.
	 * <p>
	 * @return {@code true} if is read only, {@code false} if is read / write.
	 */
	public final boolean readonly() { return this.readonly; }
	
	/**
	 * <p>
	 *   Gets number of parameters this have.
	 * </p>
	 * @return the parameter count.
	 */
	public final int size() { return this.data.size(); }
	
	/**
	 * <p>
	 *   Gets list of attribute names.
	 * </p>
	 * @return the list of attribute names.
	 */
	public final List<String> names() {
		return new ArrayList<>(this.data.keySet()); }
	
	/**
	 * <p>
	 *   Returns {@code true} if a value has been stored under the given name.
	 * </p>
	 * @param name the key to check
	 * @return {@code true} if a value is associated with {@code name},
	 *         otherwise {@code false}
	 * @throws IllegalArgumentException if {@code name} is empty
	 * @throws NullPointerException if {@code name} is {@code null}
	 */
	public final boolean exists(String name) {
		return this.data.containsKey(requireNonEmpty(name, "name")); }
	
	/**
	 * <p>
	 *   Returns {@code true} if a value has been stored for the given type.
	 * </p>
	 * <p>
	 *   The lookup key is {@code type.getName()}, consistent with
	 *   {@link #set(Object)} and {@link #get(Class)}.
	 * </p>
	 * @param <V>  the type whose presence should be checked
	 * @param type the class representing the stored value
	 * @return {@code true} if a value of the given type exists,
	 *         otherwise {@code false}
	 * @throws NullPointerException if {@code type} is null
	 */
	public final <V> boolean exists(Class<V> type) {
        return exists(requireNonNull(type, "type").getName()); }
	
	/**
	 * <p>
	 *   Removes all attributes from this object.
	 * </p>
	 * @throws ReadOnlyException If {@code #readonly() == true}.
	 */
	public final void clear() { requireNotReadonly(); this.data.clear(); }
	
	/**
	 * <p>
	 *   Removes named attribute.
	 * </p>
	 * @param name Name of attribute to remove.
	 * @return {@code true} if removed, {@code false} if not found.
	 * @throws ReadOnlyException If {@code #readonly() == true}. 
	 */
	public final boolean remove(String name) {
		requireNotReadonly();
		return this.data.remove(requireNonEmpty(name, "name")) != null;
	}
	
	/**
	 * <p>
	 *   Removes the value stored for the given type, if present.
	 * </p>
	 * <p>
	 *   The lookup key is {@code type.getName()}. If a value exists, it is
	 *   removed and the method returns {@code true}. If no such value was
	 *   stored, it returns {@code false}.
	 * </p>
	 * @param <V>  the type of the value to remove.
	 * @param type the class representing the stored value.
	 * @return {@code true} if a value was removed, otherwise {@code false}.
	 * @throws NullPointerException if {@code type} is null.
	 * @throws ReadOnlyException If {@code #readonly() == true}. 
	 */
	public final <V> boolean remove(Class<V> type) {
        requireNotReadonly();
        return remove(requireNonNull(type, "type").getName());
    }
	
	/**
	 * <p>
	 *   Stores the given value using its runtime class name as the key.
	 * </p>
	 * <p>
	 *   The key used is {@code value.getClass().getName()}, ensuring global
	 *   uniqueness across packages (e.g. {@code com.example.Foo} and
	 *   {@code com.other.Foo} do not collide). Only one value is stored per
	 *   concrete class; calling this method again with another instance of
	 *   the same class replaces the previous one.
	 * <p>
	 *   The value must not be {@code null}.
	 *
	 *   <h4>Example</h4>
	 *   <pre>{@code
	 *   ctx.set(new AuthContext(userId));
	 *   ctx.set(new RequestInfo(remoteAddress));
	 *
	 *   AuthContext auth = ctx.get(AuthContext.class);
	 *   }</pre>
	 * </p>
	 * @param value the non-null value to store.
	 * @return this instance for fluent chaining.
	 * @throws NullPointerException if {@code value} is null.
	 * @throws ReadOnlyException If {@code #readonly() == true}.
	 */
	public final T set(Object value) {
		requireNotReadonly();
		var named = as(Named.class, requireNonNull(value, "value"));
		var name = named == null ? value.getClass().getName() : named.name();
		return set(name, value);
	}
	
	/**
	 * <p>
	 *   Tell if attribute's value equal to given value.
	 * </p>
	 * @param name  the name of the attribute.
	 * @param value the value to check.
	 * @return {@code true} if is equal, {@code false} if is not.
	 */
	public final boolean equals(String name, Object value) {
		return this.get(requireNonEmpty(name, "name"))
			.equals(requireNonNull(value, "value")); }
	
	/**
	 * <p>
	 *   Retrieves a previously stored value by its type.
	 * </p>
	 * <p>
	 *   The lookup key is {@code type.getName()}. This method is equivalent
	 *   to retrieving the value stored by {@link #set(Object)} for the same
	 *   class. If no such value exists, an exception is thrown.
	 *
	 *   <h4>Example</h4>
	 *   <pre>{@code
	 *   AuthContext auth = ctx.get(AuthContext.class);
	 *   }</pre>
	 * </p>
	 * @param <V>  the expected value type.
	 * @param type the class object representing the value type.
	 * @return the stored value, cast to the given type.
	 * @throws NullPointerException if {@code type} is null.
	 * @throws IllegalArgumentException if no value has been stored for this
	 *         type.
	 */
	public final <V> V get(Class<V> type) {
		return get(requireNonNull(type, "type").getName(), type); }
	
	/**
	 * <p>
	 *   Sets attribute.
	 * </p>
	 * @param name  Attribute name.
	 * @param value Attribute value.
	 * @return {@code this}.
	 * @throws IllegalArgumentException if {@code name} is {@code null} or the
	 *         empty string.
	 * @throws NullPointerException if {@code value} is {@code null}.
	 * @throws ReadOnlyException If {@code #readonly() == true}. 
	 */
	public final T set(String name, Object value) {
		requireNotReadonly();
		this.data.put(requireNonEmpty(name, "name"),
			requireNonNull(value, "value"));
		return self();
	}
	
	/**
	 * <p>
	 *   Gets object indexed by {@code name}.
	 * </p>
	 * @param name the name.
	 * @return the object indexed by {@code name}.
	 * @throws NotFoundException if no object indexed by {@code name}.
	 */
	public final Object get(String name) {
		if (!this.exists(requireNonEmpty(name, "name")))
			throw new NoSuchElementException(name); 
		return data.get(name);
	}
	
	/**
	 * <p>
	 *   Gets object indexed by {@code name} or {@code def} if not found.
	 * </p>
	 * @param name the name.
	 * @param def  the default name, required not to be {@code null}.
	 * @return the object.
	 */
	public final Object get(String name, Object def) {
		requireNonEmpty(name, "name");
		requireNonNull(def, "def");
		var retVal = this.data.get(name);
		return retVal == null ? def : retVal;
	}
	
	public final String getAsString(String name) {
		return this.get(requireNonEmpty(name, "name")).toString(); }
	
	public final String getAsString(String name, String def) {
		requireNonEmpty(name, "name");
		requireNonNull(def, "def");
		return this.get(name, def).toString(); 
	}
	
	public final <V> V get(String name, Class<V> type) {
		requireNonEmpty(name, "name");
		requireNonNull(type);
		return resolve(name, this.get(name), type);
	}
	
	public final <V> V get(String name, Class<V> type, V def) {
		requireNonEmpty(name, "name");
		requireNonNull(type);
		return resolve(name, this.get(name, def), type);
	}
	
	private static final <V> V resolve(String name, Object o, Class<V> type) {
		V retVal = as(type, o);
		if (retVal != null) return retVal;
		
		try {
			var s = o.toString();
			var con = type.getConstructor(String.class);
			retVal = con.newInstance(s);
			return retVal;
		} catch (Exception x) {
			throw new IllegalArgumentException(quote(name) +
				" not available as type : " + quote(type.getName()), x);
		}
	}
	
	public final char getAsChar(String name, char def) {
		var o = this.get(name, def);
	    return objectToChar(name, o);
	}
	
	public final char getAsChar(String name) {
		return objectToChar(name, this.get(name)); }
	
	private static final char objectToChar(String name, Object o) {
		var charObj = as(Character.class, o);
		if (charObj != null) return charObj;
		
		var s = o.toString();
		if (s.length() == 1) return s.charAt(0);
		throw new IllegalArgumentException(quote(name) +
			" not available as type 'char'");
	}
	
	public final boolean getAsBoolean(String name, boolean def) {
		var o = this.get(name, def);
		var boolObj = as(Boolean.class, o);
		return boolObj == null ? parseBoolean(o.toString()) : boolObj;
	}
	
	public final boolean getAsBoolean(String name) {
		var o = this.get(name);
		var boolObj = as(Boolean.class, o);
		return boolObj == null ? parseBoolean(o.toString()) : boolObj;
	}
	
	public final short getAsShort(String name, short def) {
		var o = this.get(name, def);
		var shortObj = as(Short.class, o);
		return shortObj == null ? parseShort(o.toString()) : shortObj;
	}
	
	public final short getAsShort(String name) {
		var o = this.get(name);
		var shortObj = as(Short.class, o);
		return shortObj == null ? parseShort(o.toString()) : shortObj;
	}
	
	public final int getAsInt(String name, int def) {
		var o = this.get(name, def);
		var intObj = as(Integer.class, o);
		return intObj == null ? parseInt(o.toString()) : intObj;
	}
	
	public final int getAsInt(String name) {
		var o = this.get(name);
		var intObj = as(Integer.class, o);
		return intObj == null ? parseInt(o.toString()) : intObj;
	}
	
	public final long getAsLong(String name) {
		var o = this.get(name);
		var longObj = as(Long.class, o);
		return longObj == null ? parseLong(o.toString()) : longObj;
	}
	
	public final long getAsLong(String name, long def) {
		var o = this.get(name, def);
		var longObj = as(Long.class, o);
		return longObj == null ? parseLong(o.toString()) : longObj;
	}
	
	public final float getAsFloat(String name) {
		var o = this.get(name);
		var floatObj = as(Float.class, o);
		return floatObj == null ? parseFloat(o.toString()) : floatObj;
	}
	
	public final float getAsFloat(String name, float def) {
		var o = this.get(name, def);
		var floatObj = as(Float.class, o);
		return floatObj == null ? parseFloat(o.toString()) : floatObj;
	}
	
	public final double getAsDouble(String name) {
		var o = this.get(name);
		var doubleObj = as(Double.class, o);
		return doubleObj == null ? parseDouble(o.toString()) : doubleObj;
	}
	
	public final BigDecimal getAsBigDecimal(String name) {
		var o = this.get(name);
		var bdObj = as(BigDecimal.class, o);
		return bdObj == null ? new BigDecimal(o.toString()) : bdObj;
	}
	
	public final Map<String, Object> asMap() { return Map.copyOf(this.data); }
	
	// TODO: Index based lookup
	
	// Objects override
	
	public @Override String toString() { return this.data.toString(); }
	
	// Private
	
	// Used internally check if is not read only in "write" methods.
	private T requireNotReadonly() {
		if (readonly) throw new ReadOnlyException();
		return self();
	}
}
