package astrogeist.engine.Observatory;

public class Demo {

	public static void main(String[] args) {
		var observatory = new InstrumentNode("Observatory");
		
		observatory.with("Telescopes");
		
			observatory.from("Telescopes")
				.with("LS60MT").apperature(70)
					.description("Lunt 60mm modular solar telescope")
				.with("B1200").description("Bloking filter")
				.with("LS60Ha").serialNumber("0000977").apperature(60)
				.isSystem("LS60MT-SS")
					.description("Single stacked for H-Alpha observing / imagining")
				.with("LS60FHa").serialNumber("0000753").apperature(60)
				.isSystem("LS60MT-DS")
					.description("Double stacked for H-Alpha observing / imagining");
			
			observatory.from("LS60MT")
				.with("lunt-herschel")
				.isSystem("LS60MT-white-light")
					.description("Observing / imaging sun in white light");
			
			observatory.from("LS60MT")
				.with("WO-Diagonal")
				.description("Star diagonal")
				.isSystem("LS60MT-night");
			
			observatory.from("Telescopes")
				.with("S50").description("Seestar 50").apperature(50)
				.isSystem("S50-Night")
				.with("S50-solar-filter")
				.isSystem("S50-white-light")
					.description("Observing / imaging sun in white light");
			
		var zwoPlanetaryCam = new Instrument("ZWO-ASI-174MM")
			.description("ZWO ASI 174MM USB 3.0 Mono Camera (primary a planetary camera)");
		var powerMate2x = new Instrument("powermate-2x")
			.description("Tele Vue Powermates 2.5x / 1.25");
		var pentaxZoomEyepiece = new Instrument("Pentax Zoom 8-24mm");
		
		observatory.with("Visual");
		
			observatory.from("Visual")
				.with(pentaxZoomEyepiece)
				.with(powerMate2x)
					.isSystem("visual-with-pentax-zoom")
						.description("Visual observing with powermate 2x");
		
		observatory.with("Imaging");
		
			observatory.from("Imaging")
				.with(zwoPlanetaryCam)
					.isSystem("imaging-with-zwo-asi-174mm")
				.with(powerMate2x)
					.isSystem("imaging-with-zwo-asi-174mm-and-pm2x")
						.description("Using ZWO ASI 174 MM camera with Tele Vue powermate 2x");
			
		observatory.with("Mounts");
		
			observatory.from("Mounts")
				.with("SolarQuest")
					.description("AZ mount for solar observing only: finds and track sun");
			

		var pv = new PrintInstrumentNodeVisitor();
		observatory.dfs(pv);
		System.out.println(pv.toString());
		System.out.println();
		
		var config1 = new Configuration(observatory)
			.addSystem("LS60MT-SS")
			.addSystem("imaging-with-zwo-asi-174mm")
			.addSystem("SolarQuest");
		var config2 = new Configuration(observatory)
			.addSystem("LS60MT-SS")
			.addSystem("imaging-with-zwo-asi-174mm-and-pm2x")
			.addSystem("SolarQuest");
		var config3 = new Configuration(observatory)
			.addSystem("LS60MT-DS")
			.addSystem("imaging-with-zwo-asi-174mm")
			.addSystem("SolarQuest");
		var config4 = new Configuration(observatory)
			.addSystem("LS60MT-DS")
			.addSystem("imaging-with-zwo-asi-174mm-and-pm2x")
			.addSystem("SolarQuest");
		var config5 = new Configuration(observatory)
			.addSystem("LS60MT-white-light")
			.addSystem("imaging-with-zwo-asi-174mm")
			.addSystem("SolarQuest");
		var config6 = new Configuration(observatory)
			.addSystem("LS60MT-white-light")
			.addSystem("imaging-with-zwo-asi-174mm-and-pm2x")
			.addSystem("SolarQuest");
		
		System.out.println(config1);
		System.out.println(config2);
		System.out.println(config3);
		System.out.println(config4);
		System.out.println(config5);
		System.out.println(config6);
	}

}
