package aha.common.util;

import static aha.common.util.Strings.quote;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;
import static aha.common.guard.StringGuards.requireNonEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 *   A small, ordered collection of elements identified by a unique name.
 * </p>
 * <p>
 *   This type combines:
 * </p>
 * <ul>
 *   <li>
 *     <b>List semantics</b>: stable iteration order, index-based access
 *   </li>
 *   <li>
 *     <b>Name semantics</b>: lookup and removal by name, enforced uniqueness
 *   </li>
 * </ul>
 *
 * <h2>Design</h2>
 * <p>
 *   Internally, this class maintains:
 * </p>
 * <ul>
 *   <li>an {@link ArrayList} for order and index-based access</li>
 *   <li>a {@link HashMap} for name-based lookup and collision detection</li>
 * </ul>
 *
 * The internal invariant is:
 * <pre>
 *   byName contains exactly the same elements as items, keyed by their name
 * </pre>
 *
 * All mutations are controlled by this class to keep the invariant intact.
 *
 * <h2>Naming</h2>
 * <p>
 *   The name of an element is derived using a {@link Function} supplied at
 *   construction time. If none is provided, {@link Object#toString()} is used.
 * </p>
 * <p>
 *   Names must be:
 * </p>
 * <ul>
 *   <li>non-null</li>
 *   <li>non-blank</li>
 *   <li>unique within the list</li>
 * </ul>
 *
 * <h2>Complexity</h2>
 * <ul>
 *   <li>{@code get(int)}: O(1)</li>
 *   <li>{@code get(String)}: O(1) average</li>
 *   <li>{@code add(T)}: O(1) average</li>
 *   <li>{@code remove(String)}: O(N) due to list removal</li>
 * </ul>
 * <p>
 *   Intended for small collections where predictable ordering and simple
 *   name-based access are more important than raw performance.
 * </p>
 * @param <T> element type
 */
public final class NamedList<T> implements Iterable<T> {

  private final List<T> items = new ArrayList<>();
  private final Map<String, T> byName = new HashMap<>();
  private final Function<? super T, String> nameOf;

  /**
   * <p>
   *   Creates a {@code NamedList} using the supplied function to extract
   *   the name of each element.
   * </p>
   * @param nameOf function mapping an element to its name
   * @throws NullPointerException if {@code nameOf} is {@code null}
   */
  public NamedList(Function<? super T, String> nameOf) {
	  this.nameOf = requireNonNull(nameOf, "nameOf"); }

  /**
   * <p>
   *   Creates a {@code NamedList} where element names are derived from
   *   {@link Object#toString()}.
   * </p>
   * <p>
   *   This is intended as a convenience for internal tooling, diagnostics,
   *   or cases where {@code toString()} is already stable and meaningful.
   * </p>
   */
  public NamedList() { this(Object::toString); }
  
  /**
   * <p>
   *   Creates a shallow copy of another {@code NamedList}.
   * </p>
   * <p>
   *   The new instance:
   * </p>
   * <ul>
   *   <li>has its own internal list and map</li>
   *   <li>preserves element order</li>
   *   <li>shares element references with the source</li>
   *   <li>uses the same name-extraction function</li>
   * </ul>
   * <p>
   *   Modifications to this instance do not affect the source list,
   *   and vice versa.
   * </p>
   * @param other the {@code NamedList} to copy
   * @throws NullPointerException if {@code other} is {@code null}
   */
  public NamedList(NamedList<T> other) {
	  Objects.requireNonNull(other, "other");
	  this.nameOf = other.nameOf;

	  for (T element : other.items) {
		  var name = requireValidName(nameOf.apply(element));
		  items.add(element);
		  byName.put(name, element);
	  }
  }

  /**
   * <p>
   *   Returns a shallow copy of this {@code NamedList} with the same naming
   *   rule.
   * </p>
   * @return the copy.
   */
  public final NamedList<T> copy() {
	  NamedList<T> c = new NamedList<>(this.nameOf);
	  for (T e : this.items) c.add(e);
	  return c;
  }
  
  /**
   * <p>
   *   Returns the number of elements in this list.
   * </p>
   * @return the number of elements.
   */
  public final int size() { return this.items.size(); }
  
  /**
   * <p>
   *   Tells if list is empty.
   * </p>
   * @return {@code size() == 0}.
   */
  public final boolean isEmpty() { return this.items.isEmpty(); }
  
  /**
   * <p>
   *   Tells if contains element with given name (key).
   * </p>
   * @param the name the name to check.
   * @return {@code true} if contains else {@code false}.
   * @throws NullPointerException if {@code name} is {@code null}.
   * @throws IllegalArgumentException if {@code name} is the empty string.
   */
  public final boolean containsKey(String name) {
	  return this.byName.containsKey(requireNonEmpty(name, "name")); }
  
  /**
   * <p>
   *   Gets first element of this list.
   * </p>
   * @return First element.
   * @throws NoSuchElementException if this list is empty.
   */
  public final T getFirst() { return this.items.getFirst(); }
  
  /**
   * <p>
   *   Gets last element of this list.
   * </p>
   * @return Last element.
   * @throws NoSuchElementException if this list is empty.
   */
  public final T getLast() { return this.items.getLast(); }

  /**
   * <p>
   *   Returns the element at the specified position.
   * </p>
   * @param index zero-based index
   * @return the element at {@code index}
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public final T get(int index) { return this.items.get(index); }

  /**
   * <p>
   *   Returns the element with the given name.
   * </p>
   * @param name the element name
   * @return the element if found else {@code null}
   * @throws NullPointerException if {@code name} is {@code null}
   */
  public final T get(String name) {
	  return this.byName.get(requireNonEmpty(name, "name")); }
  
  /**
   * <p>
   *   Returns the element registered under the given name.
   * </p>
   * @param name the element name
   * @return the element
   * @throws NoSuchElementException if no element with {@code name} exists
   * @throws NullPointerException   if {@code name} is {@code null}
   */
  public final T getOrThrow(String name) {
	  T value = this.byName.get(requireNonNull(name, "name"));
	  if (value == null)
		  throw new NoSuchElementException("No element named " + quote(name));
	  return value;
  }

  /**
   * <p>
   *   Adds an element to the end of the list.
   * </p>
   * @param element the element to add.
   * @return {@code true} (as specified by {@link Collection#add}).
   * @throws IllegalArgumentException if an element with the same name
   *                                  already exists
   * @throws NullPointerException     if {@code element} or its name is 
   *                                  {@code null}.
   */
  public final boolean add(T element) {
	  requireNonNull(element, "element");

	  var name = requireValidName(this.nameOf.apply(element));
	  if (this.byName.containsKey(name))
		  throw new IllegalArgumentException("Name collision: " + quote(name));

	  this.items.add(element);
	  this.byName.put(name, element);
	  return true;
  }

  /**
   * <p>
   *   Removes the element with the given name, if present.
   * </p>
   * @param name the element name.
   * @return {@code true} if an element was removed.
   * @throws NullPointerException if {@code name} is {@code null}.
   */
  public final boolean remove(String name) {
	  T element = this.byName.remove(requireNonNull(name, "name"));
	  if (element == null) return false;
	  return this.items.remove(element);
  }
  
  /**
   * <p>
   *   Removes and returns the element registered under the given name.
   * </p>
   * <p>
   *   The element is removed from both the ordered list and the internal
   *   name index, preserving the class invariant.
   * </p>
   * @param name the name of the element to remove.
   * @return the removed element.
   * @throws NoSuchElementException if no element with {@code name} exists.
   * @throws NullPointerException   if {@code name} is {@code null}.
   */
  public T removeAndReturn(String name) {
	  requireNonNull(name, "name");
	  T existing = this.byName.remove(name);
	  if (existing == null) throw new NoSuchElementException(
	      "No element named " + quote(name));
	  this.items.remove(existing);
	  return existing;
  }

  /**
   * <p>
   *   Removes the specified element from the list, if present.
   * </p>
   * @param element the element to remove
   * @return {@code true} if the element was removed.
   * @throws NullPointerException if {@code element} is {@code null}.
   */
  public final boolean remove(T element) {
	  var name = this.nameOf.apply(requireNonNull(element, "element"));
	  T mapped = this.byName.get(name);

	  if (!Objects.equals(mapped, element)) return false;

	  this.byName.remove(name);
	  return this.items.remove(element);
  }

  /**
   * <p>
   *   Returns the index of the specified element, or {@code -1} if not present.
   * </p>
   * @return the index or {@code -1} if not found.
   */
  public final int indexOf(T element) { return this.items.indexOf(element); }
  
  /**
   * <p>
   *   Inserts {@code element} immediately after the element currently
   *   registered under {@code existingName}.
   * </p>
   * @param existingName the name of the anchor element.
   * @param element      the element to insert.
   * @throws NoSuchElementException   if {@code existingName} does not exist.
   * @throws IllegalArgumentException if {@code element}'s name collides with an
   *                                  existing name.
   * @throws NullPointerException     if any argument is {@code null}.
   */
  public final void addAfterNamed(String existingName, T element) {
	  insertRelativeTo(existingName, element, /*after*/ true); }

  /**
   * <p>
   *   Inserts {@code element} immediately before the element currently
   *   registered under {@code existingName}.
   * </p>
   * @param existingName the name of the anchor element.
   * @param element      the element to insert.
   * @throws NoSuchElementException   if {@code existingName} does not exist.
   * @throws IllegalArgumentException if {@code element}'s name collides with an
   *                                  existing name.
   * @throws NullPointerException     if any argument is {@code null}.
   */
  public final void addBeforeNamed(String existingName, T element) {
	  insertRelativeTo(existingName, element, /*after*/ false); }

  private final void insertRelativeTo(String existingName, T element, 
	  boolean after) {
	  
	  requireNonNull(existingName, "existingName");
	  requireNonNull(element, "element");

	  T anchor = this.byName.get(existingName);
	  if (anchor == null)
		  throw new NoSuchElementException("No element named " + 
		      quote(existingName));

	  var newName = requireValidName(this.nameOf.apply(element));
	  if (this.byName.containsKey(newName))
          throw new IllegalArgumentException("Name collision: " +
	          quote(newName));
    
	  int anchorIndex = this.items.indexOf(anchor);
	  int insertIndex = after ? anchorIndex + 1 : anchorIndex;

	  this.items.add(insertIndex, element);
	  this.byName.put(newName, element);
  }
  
  /**
   * <p>
   *   Replaces the element currently registered under the given name.
   * </p>
   * <p>
   *   The replacement occurs at the same list position, preserving order.
   *   The name under which the replacement is indexed is derived from the
   *   replacement object using the configured name extractor.
   * </p>
   * @param name        the name of the element to replace.
   * @param replacement the new element.
   * @return the element that was replaced.
   * @throws NoSuchElementException   if no element with {@code name} exists.
   * @throws IllegalArgumentException if the replacement's derived name
   *                                  collides with another element.
   * @throws NullPointerException     if any argument is {@code null}.
   */
  public T replaceNamed(String name, T replacement) {
	  requireNonNull(name, "name");
	  requireNonNull(replacement, "replacement");

	  T existing = this.byName.get(name);
	  if (existing == null)
		  throw new NoSuchElementException("No element named " + quote(name));

	  var newName = requireValidName(this.nameOf.apply(replacement));

	  // Allow replacing with same logical name, but forbid collisions with
	  // others
	  T mapped = this.byName.get(newName);
	  if (mapped != null && mapped != existing)
		  throw new IllegalArgumentException("Name collision: " + 
		      quote(newName));

	  int index = items.indexOf(existing);

	  this.items.set(index, replacement);
	  this.byName.remove(name);
	  this.byName.put(newName, replacement);

	  return existing;
  }

  /**
   * <p>
   *   Returns an unmodifiable view of the underlying list.
   * </p>
   * @return the unmodifiable view of the underlying list.
   */
  public final List<T> values() { return unmodifiableList(this.items); }

  @Override public final Iterator<T> iterator() { 
	  return this.items.iterator(); }
  
  /**
   * <p>
   *   Returns the names of all elements in this list, in iteration order.
   * </p>
   * <p>
   *   The returned list is unmodifiable and contains a snapshot of the names at
   *   the time of this call.
   * </p>
   * @return an unmodifiable list of element names.
   */
  public List<String> names() {
	  if (items.isEmpty()) return List.of();
	  var result = new ArrayList<String>(items.size());
	  for (T e : items) result.add(nameOf.apply(e));
	  return List.copyOf(result);
  }

  private static final String requireValidName(String name) {
	  if (requireNonNull(name, "name").isBlank())
		  throw new IllegalArgumentException("Name must not be blank.");
	  return name;
  }

}
