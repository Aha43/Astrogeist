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
 *   Loads {@link Scanner} components.
 * </p>
 */
public final class PluginLoader {
    private PluginLoader() { Common.throwStaticClassInstantiateError(); }
    
    private final static Logger _logger = Log.get(PluginLoader.class);

    /**
     * <p>
     *   Resolves a 
     *   {@link PluginScanner} by its qualified class name.
     * </p>
     * @param qcn the Qualified Class Name of 
     *            {@link PluginScanner} component to resolve.
     * @param locations the "locations" arguments to scanner's constructor.  
     */
    public static List<PluginScanner> resolveFactory(String qcn, List<String> locations) throws Exception {
		_logger.info("Load scanners type : '" + qcn + "'");
		
		Class<?> raw = Class.forName(qcn);
		Class<? extends PluginScanner> cls = raw.asSubclass(PluginScanner.class);
		Constructor<? extends PluginScanner> ctor = cls.getDeclaredConstructor(String.class);
		ctor.setAccessible(true);
		var retVal = new ArrayList<PluginScanner>();
		for (var l : locations) {
			_logger.info("Create scanner for location: '" + l + "'");
			var scanner = Optional.of(ctor.newInstance(l));
			if (scanner.isPresent()) retVal.add(scanner.get());
		}
		return retVal;
    }
    
}
