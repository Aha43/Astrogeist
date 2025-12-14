package aha.common.numbers.validator;

import aha.common.numbers.FunctionalNumberException;
import aha.common.numbers.UnitNumber;

public final class RequireRealFunctionalNumberValidator
	extends AbstractUnitNumberValidator {

	public static final UnitlNumberValidator INSTANCE = 
		new RequireRealFunctionalNumberValidator();
	
	private RequireRealFunctionalNumberValidator() { }
	
	@Override protected final void doValidate(UnitNumber number) { 
		if (Double.isNaN(number.value())) throw new 
			FunctionalNumberException("is not a number");
	}
}
