package aha.common.numbers.validator;

import static java.util.Objects.requireNonNull;

import aha.common.numbers.UnitNumber;

public abstract class AbstractUnitNumberValidator 
	implements UnitlNumberValidator  {

	@Override public final void validate(UnitNumber un) {
		doValidate(requireNonNull(un, "un")); }
	
	protected abstract void doValidate(UnitNumber un);
}
