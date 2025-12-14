package aha.common.numbers.validator;

import static aha.common.guard.NumberGuards.requireNumber;

public abstract class AbstractValueUnitNumberValidator
	extends AbstractUnitNumberValidator {

	private final double val;
	
	protected AbstractValueUnitNumberValidator(double val) {
		this.val = requireNumber(val, "val"); }
	
	public final double value() { return this.val; } 
}
