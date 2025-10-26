package astrogeist.ui.swing.dialog.data.userdata;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Instant;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import astrogeist.engine.abstraction.persistence.AstrogeistStorageManager;
import astrogeist.engine.persitence.disk.userdatadefinitions.UserDataDefinitions;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.userdata.UserDataEditor;
import astrogeist.ui.swing.dialog.ModalDialogBase;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class UserDataDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private UserDataDefinitions userDataDefs;
	private Instant time;
	private UserDataIo userDataIo;
	
	private UserDataEditor editor;
	
	private UserDataDialog(
		App app, 
		AstrogeistStorageManager astrogeistStorageManager,
		Instant time, 
		UserDataIo userDataIo,
		LinkedHashMap<String, TimelineValue> userData) {
		
		super(app, "User Data");
		
		this.time = time;
		
		this.userDataIo = userDataIo;
		
		//var path = Resources.getUserDataDefinitionsFile().toPath();
		try {
			//this.userDataDefs = UserDataDefinitions.fromXml(path);
			this.userDataDefs = astrogeistStorageManager.load(UserDataDefinitions.class);
			
			this.editor = new UserDataEditor(this.userDataDefs.getUserDataDefinitions(), userData);
			super.add(this.editor, BorderLayout.CENTER);
			this.createButtons();
			super.pack();
		} catch (Exception x) {
			MessageDialogs.showError(this, "Failed to open user data definition file", x); 
		}
	}
	
	private void createButtons() {
		var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		var ok = new JButton("Ok");
		ok.addActionListener(e -> { save(); setVisible(false); });
		buttonPanel.add(ok);
		
		var cancel = new JButton("Cancel");
		cancel.addActionListener(e -> this.setVisible(false));
		buttonPanel.add(cancel);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void save() {
		try {
			var values = this.editor.getValues();
			this.userDataIo.save(this.time, values);
			super.app.getTimelinePanel().update(this.time, values);
		} catch (Exception x) {
			MessageDialogs.showError(this, "Failed to save user data", x); 
		}
	}
	
	public static void show(
		App app,
		AstrogeistStorageManager astrogeistStorageManager,
		Instant t, 
		UserDataIo userDataIo,
		LinkedHashMap<String, TimelineValue> userData) { 
		
		new UserDataDialog(app, astrogeistStorageManager, t, userDataIo, userData).setVisible(true);
	}
}
