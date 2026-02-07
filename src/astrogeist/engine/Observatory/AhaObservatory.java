package astrogeist.engine.observatory;

import astrogeist.engine.observatory.constants.OpticalDesign;
import astrogeist.engine.observatory.types.Description;
import astrogeist.engine.observatory.types.Maker;
import astrogeist.engine.observatory.types.SerialNumber;
import astrogeist.engine.observatory.types.TelescopeAperture;
import astrogeist.engine.observatory.types.TelescopeFocalLength;
import astrogeist.engine.observatory.types.TelescopeWeight;

public final class AhaObservatory extends Observatory {
	
	public AhaObservatory() {
		super("AhaObservatory");
		
		var ls60mt = new Instrument("LS60MT")
			.set(OpticalDesign.REFRACTOR)
			.set(new TelescopeAperture(70))
			.set(new TelescopeFocalLength(420))
			.set(new TelescopeWeight(3.2))
			.set(new Maker("Lunt"));
		
		var s50 = new Instrument("S50")
			.set(OpticalDesign.REFRACTOR)
			.set(new TelescopeAperture(50))
			.set(new Maker("ZWO"));
		
		var s50solar = new Instrument("S50-Solar-Filter"); 
		
		var herschel = new Instrument("Lunt-Herschel-1.25")
			.tag(Tag.SOLAR).tag(Tag.WHITE);
		
		var zwoAsi174mm = new Instrument("ZWO-ASI-174MM");
		
		var pm25 = new Instrument("TV-Powermate-2.5x");
		
		var b1200a = new Instrument("B1200HaD")
			.set(new Description("Hydrogen alpha blocking filter in diagonal"))
			.tag(Tag.SOLAR).tag(Tag.HALPHA);
		
		var b1800a = new Instrument("B1800HaS")
			.set(new Description(
				"Hydrogen alpha blocking filter in straight through extension tube"))
			.tag(Tag.SOLAR).tag(Tag.HALPHA);
			
		var ls60Ha = new Instrument("LS60Ha")
			.set(new SerialNumber("0000977"))
			.set(new TelescopeAperture(60))
			.set(new Description("pressure tuned h-alpha etalon"))
			.tag(Tag.SOLAR).tag(Tag.HALPHA);
		
		var ls60Fha = new Instrument("LS60FHa")
			.set(new SerialNumber("0000753"))
			.set(new TelescopeAperture(60))
			.set(new Description("front mounted tilt tuned h-alpha etalon"))
			.tag(Tag.SOLAR).tag(Tag.HALPHA);
		
		var caK = new Instrument("Baader_CaK_Gen2")
			.tag(Tag.CAK);
			
		this.addInstrument(ls60mt)
			.addInstrument(s50)
			.addInstrument(herschel)
			.addInstrument(zwoAsi174mm)
			.addInstrument(pm25)
			.addInstrument(b1200a)
			.addInstrument(b1800a)
			.addInstrument(ls60Ha)
			.addInstrument(ls60Fha)
			.addInstrument(caK);
		
		var c1 = this.newConfiguration("LS60MT single stacked w b1200")
			.addInstrument(zwoAsi174mm.name())
			.addInstrument(b1200a.name())
			.addInstrument(ls60mt.name())
			.addInstrument(ls60Ha.name());
		
		var c2 = this.newConfiguration("LS60MT single stacked w b1800", c1)
			.replaceInstrument(b1200a.name(), b1800a.name());
		
		var c3 = this.newConfiguration("LS60MT double stacked w b1200", c1)
			.addInstrument(ls60Fha.name());
		
		var c4 = this.newConfiguration("LS60MT double stacked w b1800", c2)
			.addInstrument(ls60Fha.name());
		
		var c5 = this.newConfiguration("LS60MT single stacked w Powermate 2.5x and b1200", c1)
			.addInstrumentAfter(zwoAsi174mm.name(), pm25.name());
		
		var c6 = this.newConfiguration("LS60MT double stacked w Powermate 2.5x and b1200", c5)
			.addInstrument(ls60Fha.name());
		
		var c7 = this.newConfiguration("LS60MT single stacked w Powermate 2.5x and b1800", c2)
			.addInstrumentAfter(zwoAsi174mm.name(), pm25.name());
		
		var c8 = this.newConfiguration("LS60MT double stacked w Powermate 2.5x and b1800", c7)
			.addInstrument(ls60Fha.name());
		
		var c9 = this.newConfiguration("LS60MT w Herschel")
			.addInstrument(zwoAsi174mm.name())
			.addInstrument(herschel.name())
			.addInstrument(ls60mt.name());
		
		var c10 = this.newConfiguration("LS60MT w Powermate 2.5x and Herschel", c9)
			.addInstrumentAfter(zwoAsi174mm.name(), pm25.name());
		
		var c11 = this.newConfiguration("LS60MT w Baaded CaK and Herschel", c9)
			.addInstrumentAfter(zwoAsi174mm.name(), caK.name())
			.eraseTag(Tag.WHITE);
		
		this.close();
	}
	
	public static void main(String[] args) {
		var obervatory = new AhaObservatory();
		System.out.println(obervatory);
	}

}
