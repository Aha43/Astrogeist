package aha.common.util;

import static aha.common.guard.StringGuards.requireNonEmpty;

import java.util.Map;

/**
 * <p>
 *   Non extensible attribute object: A type safe map, see 
 *   {@link AttributeBase} for details.
 * </p>
 * @see AttributeBase
 */
public final class AttributeObject extends AttributeBase<AttributeObject> { 
	public AttributeObject() { super(); }
	public AttributeObject(AttributeObject o) { super(o); }
	public AttributeObject(Map<String, String> data) { super(data); }
	
	public AttributeObject(String name) {
		super();
		set("name", requireNonEmpty(name, "name"));
	}
	
	public AttributeObject description(String description) {
		return set("description", requireNonEmpty(description, "description")); 
	}
}
