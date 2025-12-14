package aha.common.numbers;

import static java.util.Objects.requireNonNull;

public record FunctionalNumber(FunctionalNumberType type, UnitNumber number) {
	public FunctionalNumber { 
		requireNonNull(type, "type").
			validate(requireNonNull(number, "number")); }
}
