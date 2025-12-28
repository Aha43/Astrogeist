package aha.common.numbers;

import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.quote;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import aha.common.abstraction.Named;
import aha.common.abstraction.Presentable;
import aha.common.util.DisplayNameUtil;

import static aha.common.util.Cast.as;

public class NamedUnitNumber implements Named, Presentable {
	private final String name;
	private final UnitNumber number;
	
	public NamedUnitNumber(String name, UnitNumber number) {
		this.name = requireNonEmpty(name, "name");
		this.number = requireNonNull(number, name);
	}
	
	@Override public final String name() { return this.name; }
	@Override public final String displayName() { return DisplayNameUtil.toDisplayName(this.name); }
	
	public final UnitNumber number() { return this.number; }
	public final double value() { return this.number.value(); }
	public final Unit unit() { return this.number.unit(); }
	
	@Override public final String toString() { 
		return this.name + " = " + quote(this.number); }
	
	@Override public final int hashCode() {
		return hash(this.name, this.number); }
	
	@Override public final boolean equals(Object o) {
		if (this == o) return true;
		var other = as(NamedUnitNumber.class, o);
		return other == null ? false : 
			this.name.equals(other.name) && this.number.equals(other.number);
	}

	@Override public final String presentation() { 
		return this.number.presentation(); }
}
