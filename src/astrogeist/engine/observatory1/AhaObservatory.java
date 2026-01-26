package astrogeist.engine.observatory1;

import astrogeist.engine.observatory.constants.OpticalDesign;
import astrogeist.engine.observatory.types.MountCapasity;
import astrogeist.engine.observatory.types.OuterDiameter;
import astrogeist.engine.observatory.types.TelescopeAperture;
import astrogeist.engine.observatory.types.TelescopeFocalLength;
import astrogeist.engine.observatory.types.TelescopeWeight;

public final class AhaObservatory extends Observatory {

	public AhaObservatory() { super("Aha-Observatory"); }
	
	@Override protected final void buildInventory(InventoryNode root) {
		root.with("Telescopes")
			.with("LS60MT")
				.having("Optical Design", OpticalDesign.REFRACTOR)
				.having(new TelescopeAperture(70))
				.having(new TelescopeFocalLength(420))
				.having(new TelescopeWeight(3.20))
				.having(new OuterDiameter(950))
				.description("Lunt 60mm modular solar telescope")
			.with("B1200").description("Bloking filter")
			.with("LS60Ha").serialNumber("0000977")
				.description("Pressure tuned etalon")
				.having(new TelescopeAperture(60))
			.isSystem("LS60MT-SS")
				.description("Single stacked for H-Alpha observing / imagining")
			.with("LS60FHa")
				.serialNumber("0000753")
				.having(new OuterDiameter(950))
			.isSystem("LS60MT-DS")
				.description("Double stacked for H-Alpha observing / imagining");
		
		root.from("LS60MT")
			.with("lunt-herschel")
			.isSystem("LS60MT-white-light")
				.description("Observing / imaging sun in white light");
		
		root.from("LS60MT")
			.with("WO-Diagonal")
				.description("Star diagonal")
			.isSystem("LS60MT-night");
		
		root.from("Telescopes")
			.with("S50").description("Seestar 50")
				.having(new TelescopeAperture(50))
			.isSystem("S50-Night")
			.with("S50-solar-filter")
			.isSystem("S50-white-light")
				.description("Observing / imaging sun in white light");
		
	var zwoPlanetaryCam = new Instrument("ZWO-ASI-174MM")
		.description("ZWO ASI 174MM USB 3.0 Mono Camera (primary a planetary camera)");
	var powerMate2x = new Instrument("powermate-2x")
		.description("Tele Vue Powermates 2.5x / 1.25");
	var pentaxZoomEyepiece = new Instrument("Pentax Zoom 8-24mm");
	
	var solarQuest = new Instrument("SolarQuest").set(new MountCapasity(5));
	
	root.with("Visual");
	
	root.from("Visual")
		.with(pentaxZoomEyepiece)
		.with(powerMate2x)
			.isSystem("visual-with-pentax-zoom")
				.description("Visual observing with powermate 2x");
	
	root.with("Imaging");
	
	root.from("Imaging")
		.with(zwoPlanetaryCam)
			.isSystem("zwo-asi-174mm")
		.with(powerMate2x)
			.isSystem("zwo-asi-174mm-and-pm2x")
				.description("Using ZWO ASI 174 MM camera with Tele Vue powermate 2x");
		
	root.with("Mounts");
	
	root.from("Mounts")
		.with(solarQuest)
			.isSystem("mount-solar-quest");
			//.description("AZ mount for solar observing only: finds and track sun");
		
	}

	@Override protected final void createConfigurations(InventoryNode root,
		Configurations configurations) {
		
		var config1 = new Configuration("HA-SS-LS60MT-ASI174MM-SQ", root)
			.addSystem("LS60MT-SS")
			.addSystem("zwo-asi-174mm", "Camera")
			.addSystem("mount-solar-quest", "Mount");
		var config2 = new Configuration("HA-SS-LS60MT-ASI174MM-PM2X-SQ", root)
			.addSystem("LS60MT-SS")
			.addSystem("zwo-asi-174mm-and-pm2x", "Camera")
			.addSystem("mount-solar-quest", "Mount");
		var config3 = new Configuration("HA-DS-LS60MT-ASI174MM-SQ", root)
			.addSystem("LS60MT-DS")
			.addSystem("zwo-asi-174mm", "Camera")
			.addSystem("mount-solar-quest", "Mount");
		var config4 = new Configuration("HA-DS-LS60MT-ASI174MM-PM2X-SQ", root)
			.addSystem("LS60MT-DS")
			.addSystem("zwo-asi-174mm-and-pm2x", "Camera")
			.addSystem("mount-solar-quest", "Mount");
		var config5 = new Configuration("WL-LS60MT-ASI174MM-SQ", root)
			.addSystem("LS60MT-white-light")
			.addSystem("zwo-asi-174mm", "Camera")
			.addSystem("mount-solar-quest", "Mount");
		var config6 = new Configuration("WL-LS60MT-ASI174MM-PM2X-SQ", root)
			.addSystem("LS60MT-white-light")
			.addSystem("zwo-asi-174mm-and-pm2x", "Camera")
			.addSystem("mount-solar-quest", "Mount");
		
		configurations.add(config1).add(config2).add(config3).add(config4)
			.add(config5).add(config6);
	}

}
