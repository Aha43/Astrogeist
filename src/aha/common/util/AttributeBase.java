package aha.common.util;

import static aha.common.util.Cast.as;
import static aha.common.util.Guards.requireNonEmpty;
import static aha.common.util.Strings.quote;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Short.parseShort;

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
	
	private final Map<String, Object> data = new HashMap<>();
	
	public final boolean exists(String name) {
		requireNonEmpty(name, "name");
		return this.data.containsKey(name);
	}
	
	public final <V> boolean exists(Class<V> type) {
        requireNonNull(type, "type");
        var name = type.getName();
        return exists(name);
    }
	
	public final boolean remove(String name) {
		requireNonEmpty(name, "name");
		return this.data.remove(name) != null;
	}
	
	public final <V> boolean remove(Class<V> type) {
        requireNonNull(type, "type");
        var name = type.getName();
        return remove(name);
    }
	
	public final T set(Object value) {
		requireNonNull(value, "value");
		var name = value.getClass().getName();
		return set(name, value);
	}
	
	public final <V> V get(Class<V> type) {
		requireNonNull(type, "type");
		var name = type.getName();
		return get(name, type);
	}
	
	public final T set(String name, Object value) {
		requireNonEmpty(name, "name");
		requireNonNull(value, "value");
		this.data.put(name, value);
		return self();
	}
	
	
	
	public final Object get(String name) {
		if (!this.exists(name)) throw new IllegalArgumentException(quote(name) + 
			" does not exist");
		return data.get(name);
	}
	
	public final Object get(String name, Object def) {
		requireNonEmpty(name, "name");
		requireNonNull(def, "def");
		
		var o = this.data.get(name);
		return o == null ? def : o;
	}
	
	public final String getAsString(String name) {
		return this.get(name).toString(); }
	
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
	
	public @Override String toString() { return this.data.toString(); }
}
