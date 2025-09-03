package astrogeist.engine.scanner;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import astrogeist.Common;
import astrogeist.engine.abstraction.PluginScanner;
import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.logging.Log;

/**
 * <p>
 *   Loads {@link ScannerFactory} objects.
 * </p>
 */
public final class PluginLoader {
    private PluginLoader() { Common.throwStaticClassInstantiateError(); }
    
    private final static Logger _logger = Log.get(PluginLoader.class);

    /**
     * <p>
     *   Resolve a ScannerFactory by `type`.
     *   <ul>
     *     <li>If `type` contains a dot, it is treated as a fully-qualified class name and loaded via reflection.</li>
     *     <li>Otherwise, it is matched against ServiceLoader-registered factories by `factory.id()` (case-sensitive)
     *         and, as a convenience, the implementation simpleName.</li>
     *   </ul>
     * </p>
     */
    public static List<Scanner> resolveFactory(String type, List<String> locations) throws Exception {
		_logger.info("Load scanners type : '" + type + "'");
		Class<?> raw = Class.forName(type);
		Class<? extends PluginScanner> cls = raw.asSubclass(PluginScanner.class);
		Constructor<? extends PluginScanner> ctor = cls.getDeclaredConstructor(String.class);
		ctor.setAccessible(true);
		var retVal = new ArrayList<Scanner>();
		for (var l : locations) {
			_logger.info("Create scanner for location: '" + l + "'");
			var scanner = Optional.of(ctor.newInstance(l));
			if (scanner.isPresent()) retVal.add(scanner.get());
		}
		return retVal;
    }
    
}
