package astrogeist.engine.observatory;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.guard.StringGuards.requireNotHaveAny;

public final class Tag {
	private Tag() { throwStaticClassInstantiateError(); }
	
	public final static String normalize(String tag) {
		tag = requireNonEmpty(tag, "tag").trim().toLowerCase();
		tag = requireNotHaveAny("| ", tag, "tag");
		return tag;
	}
	
	public static final String SOLAR = "solar";
	public static final String HALPHA = "halpha";
	public static final String CAK = "cak";
	public static final String WHITE = "white";
	public static final String NIGHT = "night";
}
