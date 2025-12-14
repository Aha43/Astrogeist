package aha.common.numbers.validator;

import aha.common.numbers.FunctionalNumberException;
import aha.common.numbers.UnitNumber;

import static aha.common.util.Strings.quote;

public final class RequireLargerUnitNumberValidator 
	extends AbstractValueUnitNumberValidator {
	
	public RequireLargerUnitNumberValidator(double min) { super(min); }

	@Override protected final void doValidate(UnitNumber un) {
		if (un.value() < super.value())
			throw new FunctionalNumberException(quote(un) + " < " +
				quote(super.value()));
	}

}
