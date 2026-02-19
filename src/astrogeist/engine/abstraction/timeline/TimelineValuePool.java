package astrogeist.engine.abstraction.timeline;

import java.nio.file.Path;

import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;

/**
 * <p>
 *   Interface for object that pool time line values.
 * </p>
 * <p>
 *   So since astrogeist is using this the value "1000" for say frame count will
 *   only be stored once even if appear multiple times in time line snapshots.  
 * </p>
 */
public interface TimelineValuePool {
	
	/**
	 * <p>
	 *   Gets the named
	 *   {@link TimelineValue}. Creates if this is the first {@code value}
	 *   encountered.
	 * </p>
	 * @param name  the name of the value. This will be resolved to a 
	 *              {@link Type}. The default type is
	 *              {@link Type#Text()}.
	 * @param value the value of the given resolved
	 *              {@link Type}.
	 * @return      the found or created
	 *              {@link TimelineValue}.
	 */
	TimelineValue get(String name, String value);
	
	/**
	 * <p>
	 *   Gets the named
	 *   {@link TimelineValue}. Creates if this is the first {@code value}
	 *   encountered.
	 * </p>
	 * @param type  {@link Type} of the {@code value}.
	 * @param value the value of the given resolved {@code type}.
	 * @return      the found or created
	 *              {@link TimelineValue}.
	 */
	TimelineValue get(Type type, String value);
	
	/**
	 * <p>
	 *   Gets the
	 *   {@link TimelineValue} for a {@link Type#DiskFile()} type value
	 *   ({@code path}). Creates if this is the first {@code path}
	 *   encountered.
	 * </p>
	 * @param path {@link Path} to the file.
	 * @return     the found or created
	 *             {@link TimelineValue}.
	 */
	TimelineValue getFileValue(Path path);
}
