package astrogeist.ui.swing.component.data.timeline;

import java.time.Instant;
import java.util.LinkedHashMap;

import javax.swing.JButton;

import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timeline.view.AbstractTimelineViewTablePanel;
import astrogeist.ui.swing.dialog.data.userdata.UserDataDialog;
import astrogeist.ui.swing.dialog.message.MessageDialogs;
import astrogeist.ui.swing.dialog.timeline.mapping.TimelineMappingDialog;

public final class TimelineTablePanel extends AbstractTimelineViewTablePanel {
	private static final long serialVersionUID = 1L;
	
	private final AstrogeistStorageManager astrogeistStorageManager;
	
	private final UserDataIo userDataIo; 
	
	public TimelineTablePanel(
		App app,
		AstrogeistStorageManager astrogeistStorageManager,
		UserDataIo userDataIo,
		TimelineNames timelineNames,
		SnapshotSelectionService snapshotSelectionService) {
		super(app, new TimelineTableModel(), timelineNames, snapshotSelectionService);
		
		this.astrogeistStorageManager = astrogeistStorageManager;
		this.userDataIo = userDataIo;
		
		addButtons();
	}
	
	private final TimelineTableModel model() { return (TimelineTableModel)super.model; }
	
	public final void update(Instant t, LinkedHashMap<String, TimelineValue> values) { 
		var model = (TimelineTableModel)super.model;
		model.update(t, values); 
	}
	
	private final void addButtons() {
		var mappingButton = new JButton("Mapping");
		mappingButton.addActionListener(e -> TimelineMappingDialog.show(this.app));
		super.southPanel.add(mappingButton);
		
		var userPropsButton = new JButton("User Data");
		userPropsButton.addActionListener(e -> editUserData());
		super.southPanel.add(userPropsButton);
	}
	
	private final void editUserData() {
		try {
			var selectedRow = this.getSelectedRow();
			if (selectedRow == -1) return;
		
			var t = this.getTimestampAt(selectedRow);
			var userData = this.userDataIo.load(t);
			
			UserDataDialog.show(this.app, this.astrogeistStorageManager,  t, this.userDataIo, userData);
		} catch(Exception x) {
			MessageDialogs.showError(this, "Failed to load user data", x);
		}
	}

	public final void timeline(Timeline data) {
		var model = (TimelineTableModel)super.model;
		model.timeline(data); 
		super.postSetData();
	}
	
	public final Timeline timeline() { return model().timeline(); }
	
}
