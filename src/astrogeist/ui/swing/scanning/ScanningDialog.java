package astrogeist.ui.swing.scanning;

import java.util.List;

import astrogeist.engine.abstraction.Scanner;
import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.persitence.disk.scannerconfig.ScanningConfiguration;
import astrogeist.engine.scanner.userdata.UserDataScanner;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.ModalDialogBase;

public final class ScanningDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private final ScannersSelectionPanel scannersSelectionPanel;
	
	private final AstrogeistStorageManager astrogeistStorageManager;
	
	public ScanningDialog(
		App app,
		AstrogeistStorageManager astrogeistStorageManager,
		Timeline timeline,
		TimelineValuePool tvp) throws Exception {
		
		super(app, "Scanning...");
		
		this.astrogeistStorageManager = astrogeistStorageManager;
		
		var scanners = loadScanners(tvp);
		this.scannersSelectionPanel = new ScannersSelectionPanel();
		this.scannersSelectionPanel.setScanners(scanners);
		super.setContent(scannersSelectionPanel);
		
		super.addCloseButton(true);
		super.addButton(new RefreshAction(scannersSelectionPanel));
		super.addOkButton(new ScanAction(app, timeline, scannersSelectionPanel));
	}
	
	private final List<Scanner> loadScanners(TimelineValuePool tvp) throws Exception {
		var config = this.astrogeistStorageManager.load(ScanningConfiguration.class);
		var retVal = config.createScanners();
		var userScanner = new UserDataScanner(tvp);
		retVal.add(userScanner);
		return retVal;
	}

}
