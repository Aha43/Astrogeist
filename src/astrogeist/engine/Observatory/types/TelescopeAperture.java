package astrogeist.engine.observatory.types;

import static aha.common.guard.NumberGuards.requireEqualOrLargerThan;
import static aha.common.guard.NumberGuards.requireEqualOrLesserThan;

import aha.common.numbers.NamedUnitNumber;
import aha.common.numbers.Unit;
import aha.common.numbers.UnitNumber;

public final class TelescopeAperture extends NamedUnitNumber {
	public static final String NAME = "telescope-aperture";
	public static final Unit UNIT = Unit.MM;
	public static final double MIN = 8;
	public static final double MAX = 3000;
	
	public TelescopeAperture(double v) {
		super(NAME, 
			new UnitNumber(
				requireEqualOrLargerThan(
					requireEqualOrLesserThan(v, MAX, "v"), MIN, "v"), UNIT));
	}
}
