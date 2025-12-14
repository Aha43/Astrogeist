package aha.common.numbers.validator;

import java.util.Arrays;
import java.util.List;

import aha.common.numbers.UnitNumber;

public final class CompositeUnitNumberValidator 
	extends AbstractUnitNumberValidator {
	
	private final List<UnitlNumberValidator> validators;
	
	public CompositeUnitNumberValidator(
		UnitlNumberValidator ...validators) {
		
		this.validators = Arrays.asList(validators);
	}

	@Override protected final void doValidate(UnitNumber number) {
		for (var v : this.validators) v.validate(number); }
}
