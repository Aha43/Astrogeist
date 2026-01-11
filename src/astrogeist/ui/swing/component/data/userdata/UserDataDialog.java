package astrogeist.ui.swing.component.data.userdata;

import static astrogeist.ui.swing.dialog.message.MessageDialogs.showError;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Instant;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import aha.common.abstraction.io.appdata.AppDataManager;
import astrogeist.engine.abstraction.TimelineManager;
import astrogeist.engine.appdata.userdatadefinitions.UserDataDefinitions;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.ModalDialogBase;

public final class UserDataDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private UserDataDefinitions userDataDefs;
	private Instant time;
	private UserDataIo userDataIo;
	
	private UserDataEditor editor;
	
	private UserDataDialog(
		App app, 
		AppDataManager appDataManager,
		Instant time, 
		UserDataIo userDataIo,
		LinkedHashMap<String, TimelineValue> userData) {
		
		super(app, "User Data");
		
		this.time = time;
		
		this.userDataIo = userDataIo;
		
		try {
			this.userDataDefs = appDataManager.load(UserDataDefinitions.class);
			
			this.editor = new UserDataEditor(
				this.userDataDefs.getUserDataDefinitions(), userData);
			super.add(this.editor, BorderLayout.CENTER);
			this.createButtons();
			super.pack();
		} catch (Exception x) {
			showError(this, "Failed to open user data definition file", x); }
	}
	
	private final void createButtons() {
		var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		var ok = new JButton("Ok");
		ok.addActionListener(e -> { save(); setVisible(false); });
		buttonPanel.add(ok);
		
		var cancel = new JButton("Cancel");
		cancel.addActionListener(e -> this.setVisible(false));
		buttonPanel.add(cancel);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private final void save() {
		try {
			var values = this.editor.getValues();
			this.userDataIo.save(this.time, values);
			//super.app.getTimelinePanel().update(this.time, values);
			super.app.service(TimelineManager.class).update(this.time, values);
		} catch (Exception x) {
			showError(this, "Failed to save user data", x); }
	}
	
	public static void show(
		App app,
		AppDataManager appDataManager,
		Instant t, 
		UserDataIo userDataIo,
		LinkedHashMap<String, TimelineValue> userData) { 
		
		new UserDataDialog(app, appDataManager, t, userDataIo, userData)
			.setVisible(true);
	}
}
