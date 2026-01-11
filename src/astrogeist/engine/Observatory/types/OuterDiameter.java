package astrogeist.engine.observatory.types;

import static aha.common.guard.NumberGuards.requireEqualOrLargerThan;
import static aha.common.guard.NumberGuards.requireEqualOrLesserThan;

import aha.common.numbers.NamedUnitNumber;
import aha.common.numbers.Unit;
import aha.common.numbers.UnitNumber;

public final class OuterDiameter extends NamedUnitNumber {
	public static final String NAME = "outer-diameter";
	public static final Unit UNIT = Unit.MM;
	public static final double MIN = 100;
	public static final double MAX = 3100;
	
	public OuterDiameter(double v) {
		super(NAME, 
			new UnitNumber(
				requireEqualOrLargerThan(
					requireEqualOrLesserThan(v, MAX, "v"), MIN, "v"), UNIT));
	}
}
