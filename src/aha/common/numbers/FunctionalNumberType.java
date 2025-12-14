package aha.common.numbers;

import aha.common.numbers.validator.UnitlNumberValidator;
import aha.common.numbers.validator.RequireRealFunctionalNumberValidator;

public class FunctionalNumberType {
	private final String use;
	private final UnitType type;
	private UnitlNumberValidator validator = 
		RequireRealFunctionalNumberValidator.INSTANCE;
	
	private FunctionalNumberType(String use, UnitType type) {
        this.use = use;
        this.type = type;
    }
	
	public final String use() { return this.use; }
	
	public final UnitType type() { return this.type; }
	
	public final void validate(UnitNumber unitNumber) {
		if (unitNumber.unit().type() != this.type) {
			throw new FunctionalNumberException("Number must be of unit type " + 
				this.type + " but is " + unitNumber.unit().type());
		}
		this.validator.validate(unitNumber);
	}
	
}
