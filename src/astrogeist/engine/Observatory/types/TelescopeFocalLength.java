package astrogeist.engine.observatory.types;

import static aha.common.guard.NumberGuards.requireEqualOrLargerThan;
import static aha.common.guard.NumberGuards.requireEqualOrLesserThan;

import aha.common.numbers.NamedUnitNumber;
import aha.common.numbers.Unit;
import aha.common.numbers.UnitNumber;

public class TelescopeFocalLength extends NamedUnitNumber {
	public static final String NAME = "telescope-focal-length";
	public static final Unit UNIT = Unit.MM;
	public static final double MIN = 5;
	public static final double MAX = 15000;
	
	public TelescopeFocalLength(double v) {
		super(NAME, 
			new UnitNumber(
				requireEqualOrLargerThan(
					requireEqualOrLesserThan(v, MAX, "v"), MIN, "v"), UNIT));
	}
}
