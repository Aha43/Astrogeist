package aha.common.ui.swing.panels.namevalueunit;

import static aha.common.guard.Guards.requireNonEmpty;

import aha.common.tuple.Tuple2;
import aha.common.units.Unit;
import aha.common.units.UnitNumber;

import static java.util.Objects.requireNonNull;

final class NameValueUnitEntry {
    private String name;
    private UnitNumber number;

    public NameValueUnitEntry(String name, String value, Unit unit) {
        requireNonEmpty(name, "name");
        requireNonEmpty(value, "value");
        requireNonNull(unit, "unit");
        
    	this.name = name;
        
    	var n = Double.parseDouble(value);
    	this.number = new UnitNumber(n, unit);
    }

    public final String getName() { return name; }

    public final void setName(String name) { 
    	this.name = requireNonEmpty(name, "name"); }

    public final UnitNumber getValue() { return this.number; }

    public final void setValue(String value) {
    	var n = Double.parseDouble(value);
    	this.number = new UnitNumber(n, this.number.unit());
    }

    public final Unit getUnit() { return this.number.unit(); }

    // unit can be null or empty
    public final void setUnit(Unit unit) { 
    	requireNonNull(unit);
    	this.number = new UnitNumber(this.number.number(), unit);
    }
    
    public final Tuple2<String, UnitNumber> toUnitNumber() {
    	return new Tuple2<>(this.name, this.number); }

    @Override
    public String toString() {
        return "NameValueUnitEntry{" +
               "name='" + name + '\'' +
               ", value='" + this.number.number() + '\'' +
               ", unit='" + this.number.unit() + '\'' +
               '}';
    }
}

