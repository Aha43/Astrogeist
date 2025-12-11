package aha.common.numbers;

public final class UsableNumber {
	private final UsableNumberType type;
	private final UnitNumber number;
	
	public UsableNumber(UnitNumber number, UsableNumberType type) {
		type.validate(number);
		this.number = number;
		this.type = type;
	}
	
	public final UsableNumberType type() { return this.type; }
	public final UnitNumber number() { return this.number; }
}
