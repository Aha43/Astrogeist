package astrogeist.app.dialog.timeline.mapping;

import astrogeist.app.App;
import astrogeist.app.dialog.ModalDialogBase;

public final class TimelineMappingDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private TimelineMappingDialog(App app) {
		super(app, "Timeline Mapping");
		
		
	}
	
	public static void show(App app) { new TimelineMappingDialog(app).setVisible(true); }

}
