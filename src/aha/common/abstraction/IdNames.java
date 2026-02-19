package aha.common.abstraction;

import java.util.Optional;

/**
 * <p>
 *   Resolves human-friendly display names for stable ids, and optionally
 *   provides developer-friendly fallback labels when no mapping exists.
 * <p>
 *   Intended as an injectable service. Typical usage:
 *   <ul>
 *     <li>
 *       Domain/model layers: use {@link #requireName(String)} when missing
 *       mappings are a bug.
 *     </li>
 *     <li>
 *       UI/dev flows: use {@link #label(String)} to degrade visibly without
 *       throwing.
 *     </li>
 *   </ul>
 * </p>
 */
public interface IdNames {
    /**
     * <p>
     *   Tries to resolve a display name for the given id.
     * </p>
     * @param id stable id.
     * @return resolved name if present; otherwise empty.
     * @throws IllegalArgumentException if {@code id} is null/blank.
     */
    Optional<String> tryName(String id);

    /**
     * <p>
     *   Resolves a display name for the given id.
     * </p>
     * @param id stable id
     * @return resolved name.
     * @throws IllegalArgumentException if {@code id} is null/blank.
     * @throws IllegalStateException if the id is not mapped.
     */
    String requireName(String id);

    /**
     * <p>
     *   Resolves a display label for the given id.
     * </p>
     * <p>
     *   This method never throws due to missing mappings. If no mapping exists,
     *   it must return a visibly "unmapped" label so the issue stands out in UI
     *   during development.
     * </p>
     * @param id stable id.
     * @return resolved name if present; otherwise a visible fallback label.
     * @throws IllegalArgumentException if {@code id} is null/blank.
     */
    String label(String id);

    /**
     * <p>
     *   Convenience overload for objects that expose both id and name.
     * </p>
     * @param o object
     * @return {@link #tryName(String)} for {@code o.id()}.
     * @throws NullPointerException if {@code o} is null.
     */
    default Optional<String> tryName(IndexedAndNamed o) {
    	return tryName(o.id()); }

    /**
     * <p>
     *   Convenience overload for objects that expose both id and name.
     * </p>
     * @param o object.
     * @return {@link #requireName(String)} for {@code o.id()}.
     * @throws NullPointerException if {@code o} is null.
     * @throws IllegalStateException if the id is not mapped. 
     */
    default String requireName(IndexedAndNamed o) {
    	return requireName(o.id()); }

    /**
     * <p>
     *   Convenience overload for objects that expose both id and name.
     * </p>
     * @param o object.
     * @return {@link #label(String)} for {@code o.id()}.
     * @throws NullPointerException if {@code o} is null.
     */
    default String label(IndexedAndNamed o) { return label(o.id()); }

    /**
     * <p>
     *   Registers an id-to-name mapping from an
     *   {@link IndexedAndNamed} object.
     * </p>
     * @param o object to register.
     * @return this (for fluent usage).
     * @throws NullPointerException if {@code o} is null.
     * @throws IllegalArgumentException if id/name is null/blank.
     */
    default IdNames register(IndexedAndNamed o) {
    	return register(o.id(), o.name()); }

    /**
     * <p>
     *   Registers an id-to-name mapping.
     * </p>
     * <p>
     *   Contract: registrations are expected to be stable. If you want to support
     *   renames, use {@link #rename(String, String)}.
     * </p>
     * @param id stable id.
     * @param name display name.
     * @return this (for fluent usage).
     * @throws IllegalArgumentException if id/name is null/blank.
     * @throws IllegalStateException if the id is already registered.
     */
    IdNames register(String id, String name);

    /**
     * <p>
     *   Renames an already-registered id.
     * </p>
     * <p>
     *   This makes the rename operation explicit and allows implementations to
     *   enforce "must exist" semantics.
     * </p>
     * @param id stable id.
     * @param newName new display name.
     * @return this (for fluent usage).
     * @throws IllegalArgumentException if id/newName is null/blank.
     * @throws IllegalStateException if the id is not registered.
     */
    IdNames rename(String id, String newName);

    /**
     * <p>
     *   Returns true if an id is registered.
     * </p>
     * @param id stable id.
     * @return true if registered.
     * @throws IllegalArgumentException if {@code id} is null/blank.
     */
    boolean contains(String id);
}

