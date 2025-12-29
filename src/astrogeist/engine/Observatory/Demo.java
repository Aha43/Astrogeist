package astrogeist.engine.observatory;

import astrogeist.engine.observatory.types.TelescopeAperture;
import astrogeist.ui.swing.component.observatory.ConfigurationPanel;

public class Demo {

	public static void main(String[] args) {
		
		var observatory = new AhaObservatory();
		ConfigurationPanel.showDialog(observatory);
		
//		var observatory = InventoryNode.createRoot("Observatory");
//		
//		observatory.with("Telescopes");
//		
//			observatory.from("Telescopes")
//				.with("LS60MT")
//					.having(new TelescopeAperture(70))
//					.description("Lunt 60mm modular solar telescope")
//				.with("B1200").description("Bloking filter")
//				.with("LS60Ha").serialNumber("0000977")
//					.having(new TelescopeAperture(60))
//				.isSystem("LS60MT-SS")
//					.description("Single stacked for H-Alpha observing / imagining")
//				.with("LS60FHa").serialNumber("0000753")
//				.isSystem("LS60MT-DS")
//					.description("Double stacked for H-Alpha observing / imagining");
//			
//			observatory.from("LS60MT")
//				.with("lunt-herschel")
//				.isSystem("LS60MT-white-light")
//					.description("Observing / imaging sun in white light");
//			
//			observatory.from("LS60MT")
//				.with("WO-Diagonal")
//					.description("Star diagonal")
//				.isSystem("LS60MT-night");
//			
//			observatory.from("Telescopes")
//				.with("S50").description("Seestar 50")
//					.having(new TelescopeAperture(50))
//				.isSystem("S50-Night")
//				.with("S50-solar-filter")
//				.isSystem("S50-white-light")
//					.description("Observing / imaging sun in white light");
//			
//		var zwoPlanetaryCam = new Instrument("ZWO-ASI-174MM")
//			.description("ZWO ASI 174MM USB 3.0 Mono Camera (primary a planetary camera)");
//		var powerMate2x = new Instrument("powermate-2x")
//			.description("Tele Vue Powermates 2.5x / 1.25");
//		var pentaxZoomEyepiece = new Instrument("Pentax Zoom 8-24mm");
//		
//		var solarQuest = new Instrument("SolarQuest");
//		
//		observatory.with("Visual");
//		
//			observatory.from("Visual")
//				.with(pentaxZoomEyepiece)
//				.with(powerMate2x)
//					.isSystem("visual-with-pentax-zoom")
//						.description("Visual observing with powermate 2x");
//		
//		observatory.with("Imaging");
//		
//			observatory.from("Imaging")
//				.with(zwoPlanetaryCam)
//					.isSystem("zwo-asi-174mm")
//				.with(powerMate2x)
//					.isSystem("zwo-asi-174mm-and-pm2x")
//						.description("Using ZWO ASI 174 MM camera with Tele Vue powermate 2x");
//			
//		observatory.with("Mounts");
//		
//			observatory.from("Mounts")
//				.with(solarQuest)
//					.isSystem("mount-solar-quest");
//					//.description("AZ mount for solar observing only: finds and track sun");
//			
//
//		var pv = new PrintInventoryNodeVisitor();
//		observatory.dfs(pv);
//		System.out.println(pv.toString());
//		System.out.println();
//		
//		var config1 = new Configuration("HA-SS-LS60MT-ASI174MM-SQ", observatory)
//			.addSystem("LS60MT-SS")
//			.addSystem("zwo-asi-174mm", "Camera")
//			.addSystem("mount-solar-quest", "Mount");
//		var config2 = new Configuration("HA-SS-LS60MT-ASI174MM-PM2X-SQ", observatory)
//			.addSystem("LS60MT-SS")
//			.addSystem("zwo-asi-174mm-and-pm2x", "Camera")
//			.addSystem("mount-solar-quest", "Mount");
//		var config3 = new Configuration("HA-DS-LS60MT-ASI174MM-SQ", observatory)
//			.addSystem("LS60MT-DS")
//			.addSystem("zwo-asi-174mm", "Camera")
//			.addSystem("mount-solar-quest", "Mount");
//		var config4 = new Configuration("HA-DS-LS60MT-ASI174MM-PM2X-SQ", observatory)
//			.addSystem("LS60MT-DS")
//			.addSystem("zwo-asi-174mm-and-pm2x", "Camera")
//			.addSystem("mount-solar-quest", "Mount");
//		var config5 = new Configuration("WL-LS60MT-ASI174MM-SQ", observatory)
//			.addSystem("LS60MT-white-light")
//			.addSystem("zwo-asi-174mm", "Camera")
//			.addSystem("mount-solar-quest", "Mount");
//		var config6 = new Configuration("WL-LS60MT-ASI174MM-PM2X-SQ", observatory)
//			.addSystem("LS60MT-white-light")
//			.addSystem("zwo-asi-174mm-and-pm2x", "Camera")
//			.addSystem("mount-solar-quest", "Mount");
//		
//		System.out.println(config1);
//		System.out.println(config2);
//		System.out.println(config3);
//		System.out.println(config4);
//		System.out.println(config5);
//		System.out.println(config6);
//		
//		ConfigurationPanel.showDialog(config1, config2, config3, config4, config5, config6);
	}

}
