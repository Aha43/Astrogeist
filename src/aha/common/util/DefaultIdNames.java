package aha.common.util;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.isNullOrBlank;
import static aha.common.util.Strings.quote;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import aha.common.abstraction.IdNames;

/**
 * <p>
 *   Default in-memory implementation of {@link IdNames}.
 * </p>
 * <p>
 *   - Stores id-to-name mappings in a {@link HashMap}.<br>
 *   - {@link #label(String)} never fails due to missing mappings and returns a visibly
 *     "unmapped" label so issues stand out during development.
 * </p>
 * <p>
 *   Thread-safety: not thread-safe.
 * </p>
 */
public final class DefaultIdNames implements IdNames {
    private final Map<String, String> map;
    private final String unmappedPrefix;
    private final String unmappedSuffix;

    /**
     * <p>
     *   Creates a resolver with a default visible fallback label format: 
     *   {@code "<unmapped:ID>"}.
     * </p>
     */
    public DefaultIdNames() { this(new HashMap<>(), "<unmapped:", ">"); }

    /**
     * <p>
     *   Creates a resolver with a custom visible fallback label format.
     * </p>
     * @param unmappedPrefix prefix used by {@link #label(String)} when id is
     *                       not mapped.
     * @param unmappedSuffix suffix used by {@link #label(String)} when id is 
     *                       not mapped.
     */
    public DefaultIdNames(String unmappedPrefix, String unmappedSuffix) {
        this(new HashMap<>(),
            requireNonNull(unmappedPrefix, "unmappedPrefix"),
            requireNonNull(unmappedSuffix, "unmappedSuffix"));
    }

    private DefaultIdNames(Map<String, String> map, String unmappedPrefix, 
    	String unmappedSuffix) {
        
    	this.map = requireNonNull(map, "map");
        this.unmappedPrefix = requireNonNull(unmappedPrefix, "unmappedPrefix");
        this.unmappedSuffix = requireNonNull(unmappedSuffix, "unmappedSuffix");
    }

    @Override public final Optional<String> tryName(String id) {
        var key = requireNonEmpty(id, "id");
        return Optional.ofNullable(this.map.get(key));
    }

    @Override public final String requireName(String id) {
        var key = requireNonEmpty(id, "id");
        var name = this.map.get(key);
        if (name == null) {
            throw new IllegalStateException("Id is not registered: " +
            	quote(key));
        }
        return name;
    }

    @Override public final String label(String id) {
        var key = requireNonEmpty(id, "id");
        var name = this.map.get(key);
        return name != null ? name : (this.unmappedPrefix + key + 
        	this.unmappedSuffix);
    }
    
    @Override public final String labelOrSelf(String idOrLabel) {
    	if (isNullOrBlank(idOrLabel)) return Strings.EMPTY;
		return map.getOrDefault(idOrLabel, idOrLabel);
	}

    @Override public final IdNames register(String id, String name) {
        var key = requireNonEmpty(id, "id");
        var value = requireNonEmpty(name, "name");

        var existing = this.map.putIfAbsent(key, value);
        if (existing != null) {
            throw new IllegalStateException(
                "Id is already registered: '" + key + "' (existing name: " +
                	quote(existing) + ", new name: " + quote(value) + ")");
        }
        return this;
    }

    @Override public final IdNames rename(String id, String newName) {
        var key = requireNonEmpty(id, "id");
        var value = requireNonEmpty(newName, "newName");

        if (!this.map.containsKey(key)) {
            throw new IllegalStateException("Cannot rename unregistered id: " +
            	quote(key));
        }
        this.map.put(key, value);
        return this;
    }

    @Override public final boolean contains(String id) {
        return this.map.containsKey(requireNonEmpty(id, "id")); }
    
    @Override public final Map<String,String> entries() {
        return unmodifiableMap(this.map); }
}
