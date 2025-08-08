package astrogeist.engine.typesystem;

import astrogeist.Common;

public final class TypeResolver {
	
	public static Type resolve(String name, String value) {
		name = name.trim().toLowerCase();
		if (name.length() == 0) return Type.Void();
		switch (name) {
			case "binning"    : return Type.Binning();
			case "exposure"   : return Type.Exposure().resolve(value);
			case "gain"       : return Type.Gain();
			case "framecount" : return Type.FrameCount();
		}
		return Type.Text();
	}

	private TypeResolver() { Common.throwStaticClassInstantiateError(); }
}
