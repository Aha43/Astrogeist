package astrogeist.ui.swing.scanning;

import java.util.List;

import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.scanner.ScannerConfigLoader;
import astrogeist.engine.scanner.userdata.UserDataScanner;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.ModalDialogBase;

public final class ScanningDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private final ScannersSelectionPanel scannersSelectionPanel;
	
	public ScanningDialog(App app) throws Exception {
		super(app, "Scanning...");
		
		var scanners = loadScanners(app.getServices().getTimelineValuePool());
		this.scannersSelectionPanel = new ScannersSelectionPanel();
		this.scannersSelectionPanel.setScanners(scanners);
		super.setContent(scannersSelectionPanel);
		
		super.addCloseButton(true);
		super.addButton(new RefreshAction(scannersSelectionPanel));
		super.addOkButton(new ScanAction(app, scannersSelectionPanel));
	}
	
	private static final List<Scanner> loadScanners(TimelineValuePool tvp) throws Exception {
		var configFile = Resources.getScanningConfigFile();
		var config = ScannerConfigLoader.parse(configFile);
		var retVal = ScannerConfigLoader.createScanners(config);
		var userScanner = new UserDataScanner(tvp);
		retVal.add(userScanner);
		return retVal;
	}

}
