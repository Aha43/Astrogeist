package aha.common.numbers;

public class UsableNumberType {
	private final String use;
	private final UnitType type;
	private final double low;
	private final double high;
	
	private UsableNumberType(String use, UnitType type, double low, double high) {
        this.use = use;
        this.type = type;
        this.low = low;
        this.high = high;
    }
	
	public static UsableNumberType unbounded(String use, UnitType type) {
        return new UsableNumberType(use, type, Double.NaN, Double.NaN); }
	
	public static UsableNumberType between(String use, UnitType type,
		double low, double high) {
        	return new UsableNumberType(use, type, low, high); }
	
	public final String use() { return this.use; }
	
	public final UnitType type() { return this.type; }
	
	public final void validate(UnitNumber unitNumber) {
		if (unitNumber.unit().type() != this.type) {
			throw new UsuableNumberException("Number must be of unit type " + 
				this.type + " but is " + unitNumber.unit().type());
		}
		if (!Double.isNaN(this.low)) {
			if (unitNumber.number() < this.low) {
				throw new UsuableNumberException(
					unitNumber + " < " + this.low);
			}
		}
		if (!Double.isNaN(this.high)) {
			if (unitNumber.number() > this.high) {
				throw new UsuableNumberException(
					unitNumber + " > " + this.high);
			}
		}
	}
	
}
