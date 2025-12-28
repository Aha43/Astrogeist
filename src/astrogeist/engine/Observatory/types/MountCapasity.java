package astrogeist.engine.observatory.types;

import static aha.common.guard.NumberGuards.requireEqualOrLargerThan;
import static aha.common.guard.NumberGuards.requireEqualOrLesserThan;

import aha.common.numbers.NamedUnitNumber;
import aha.common.numbers.Unit;
import aha.common.numbers.UnitNumber;

public class MountCapasity extends NamedUnitNumber {
	public static final String NAME = "mount-capacity";
	public static final Unit UNIT = Unit.KG;
	public static final double MIN = 3;
	public static final double MAX = 110;
	
	public MountCapasity(double v) {
		super(NAME, 
			new UnitNumber(
				requireEqualOrLargerThan(
					requireEqualOrLesserThan(v, MAX, "v"), MIN, "v"), UNIT));
	}
}
