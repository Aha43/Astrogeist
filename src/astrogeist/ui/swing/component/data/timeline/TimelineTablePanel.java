package astrogeist.ui.swing.component.data.timeline;

import static astrogeist.ui.swing.dialog.message.MessageDialogs.showError;

import java.time.Instant;
import java.util.LinkedHashMap;

import javax.swing.JButton;

import aha.common.abstraction.io.appdata.AppDataManager;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.view.AbstractTimelineViewTablePanel;
import astrogeist.ui.swing.component.data.userdata.UserDataDialog;
import astrogeist.ui.swing.component.observatory.use.SelectSnapshotConfigurationDialogAction;
import astrogeist.ui.swing.dialog.timeline.mapping.TimelineMappingDialog;

public final class TimelineTablePanel extends AbstractTimelineViewTablePanel {
	
	private static final long serialVersionUID = 1L;
	
	private final AppDataManager astrogeistStorageManager;
	
	private final UserDataIo userDataIo; 
	
	public TimelineTablePanel(
		App app,
		AppDataManager astrogeistStorageManager,
		UserDataIo userDataIo,
		TimelineNames timelineNames,
		SnapshotSelectionService snapshotSelectionService) {
		
		super(app, astrogeistStorageManager, new TimelineTableModel(app), 
			timelineNames, snapshotSelectionService);
		
		this.astrogeistStorageManager = astrogeistStorageManager;
		this.userDataIo = userDataIo;
		
		addButtons();
	}
	
	private final TimelineTableModel model() {
		return (TimelineTableModel)super.model; }
	
	public final void update(Instant t, 
		LinkedHashMap<String, TimelineValue> values) { 
		
		var model = (TimelineTableModel)super.model;
		model.update(t, values); 
	}
	
	public final void update(Instant t, String name, TimelineValue value) {
		var model = (TimelineTableModel)super.model;
		model.update(t, name, value);
	}
	
	private final void addButtons() {
		var mappingButton = new JButton("Mapping");
		mappingButton.addActionListener(
			e -> TimelineMappingDialog.show(this.app));
		super.southPanel.add(mappingButton);
		
		var userPropsButton = new JButton("User Data");
		userPropsButton.addActionListener(e -> editUserData());
		super.southPanel.add(userPropsButton);
		
		var configurations = new JButton(
			new SelectSnapshotConfigurationDialogAction(this.app));
		super.southPanel.add(configurations);
	}
	
	private final void editUserData() {
		try {
			var selectedRow = this.getSelectedRow();
			if (selectedRow == -1) return;
		
			var t = this.getTimestampAt(selectedRow);
			var userData = this.userDataIo.load(t);
			
			UserDataDialog.show(this.app, this.astrogeistStorageManager, t,
				this.userDataIo, userData);
		} catch(Exception x) {
			showError(this, "Failed to load user data", x); }
	}

	public final void timeline(Timeline data) {
		var model = (TimelineTableModel)super.model;
		model.timeline(data); 
		super.postSetData();
	}
	
	public final Timeline timeline() { return model().timeline(); }
	
}
