package astrogeist.ui.swing.dialog.filtering;

import java.awt.BorderLayout;

import astrogeist.engine.timeline.view.PropertiesTimelineViewFilter;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.general.KeyValuePairsPanel;
import astrogeist.ui.swing.dialog.ModalDialogBase;

public final class PropertiesTimelineFilterDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private final KeyValuePairsPanel kvpp = new KeyValuePairsPanel(); 
	
	private PropertiesTimelineFilterDialog(App app) {
		super(app, "Filter");
		
		super.add(this.kvpp, BorderLayout.CENTER);
		super.pack();
		super.setSize(500, 500);
	}

	
	public static PropertiesTimelineViewFilter show(App app) {
		var dlg = new PropertiesTimelineFilterDialog(app);
		dlg.showIt();
		var result = dlg.kvpp.getPairs();
		var retVal = new PropertiesTimelineViewFilter(result);
		return retVal;
	}
}
