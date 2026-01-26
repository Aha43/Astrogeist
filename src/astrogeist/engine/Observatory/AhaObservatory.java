package astrogeist.engine.observatory;

import astrogeist.engine.observatory.constants.OpticalDesign;
import astrogeist.engine.observatory.types.Description;
import astrogeist.engine.observatory.types.Produsent;
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
			.set(new Produsent("Lunt"));
		
		var b1200a = new Instrument("B1200HaD")
			.set(new Description("Hydrogen alpha blocking filter in diagonal"));
		
		
		var b1800a = new Instrument("B1800HaS")
			.set(new Description(
				"Hydrogen alpha blocking filter in straight through extension tube"));
		
		
		var ls60Ha = new Instrument("LS60Ha")
			.set(new SerialNumber("0000977"))
			.set(new TelescopeAperture(60))
			.set(new Description("pressure tuned etalon"));
			
		
		this
			.addInstrument(ls60mt)
			.addInstrument(b1200a)
			.addInstrument(b1800a)
			.addInstrument(ls60Ha);
		
		this.newConfiguration("c1")
		
			.addInstrument("B1200a")
			.addInstrument("LS60MT")
			.addInstrument("LS60Ha");
		
		this.newConfiguration("c2", "c1")
			.replaceInstrument("B1200a", "B1800a");
		
	}
	
	public static void main(String[] args) {
		var obervatory = new AhaObservatory();
		System.out.println(obervatory);
	}

}
