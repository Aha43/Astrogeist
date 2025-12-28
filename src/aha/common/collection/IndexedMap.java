package aha.common.collection;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import aha.common.exceptions.UnreachableError;

public final class IndexedMap<K, V> implements Iterable<V> {
	private final LinkedHashMap<K, V> map = new LinkedHashMap<>();

	public final int size() { return map.size(); }
	public final boolean isEmpty() { return map.isEmpty(); }

	public final boolean containsKey(K key) { return map.containsKey(key); }
	public final V get(K key) { return map.get(key); }

	public final void put(K key, V value) {
		map.put(requireNonNull(key, "key"), requireNonNull(value, "value")); }

	public final V removeByKey(K key) { return map.remove(key); }

	/** O(N). Deterministic order (insertion order). */
	public final V getAt(int idx) {
		requireIndex(idx);
		int i = 0;
		for (V v : map.values()) if (i++ == idx) return v;
		throw new UnreachableError();
	}

	/** O(N). */
	public final K keyAt(int idx) {
		requireIndex(idx);
		int i = 0;
		for (K k : map.keySet()) if (i++ == idx) return k;
		throw new UnreachableError();
	}
	
	public final int indexOf(V value) {
		requireNonNull(value, "value");
		int i = 0;
		for (V v : map.values()) {
			if (v == value) return i;
			i++;
		}
		return -1;
	}

	/** Snapshot list view (safe to hand to UI). */
 	public final List<V> values() { return List.copyOf(map.values()); }
 	public final Set<K> keySet() { return unmodifiableSet(map.keySet()); }
 	public final Map<K,V> asMapView() { return unmodifiableMap(map); }
 	
 	public final Set<Entry<K, V>> entrySet() { 
 		return unmodifiableSet(map.entrySet()); }

 	@Override public final Iterator<V> iterator() { 
 		return map.values().iterator(); }

 	private void requireIndex(int idx) {
 		if (idx < 0 || idx >= map.size())
 			throw new IndexOutOfBoundsException("index=" + idx + ", size=" + 
 				map.size());
 	}
}
