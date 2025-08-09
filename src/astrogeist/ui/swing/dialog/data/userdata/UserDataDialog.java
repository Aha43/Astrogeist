package astrogeist.ui.swing.dialog.data.userdata;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Instant;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import astrogeist.engine.resources.Resources;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.userdata.UserDataDefinitions;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.userdata.UserDataEditor;
import astrogeist.ui.swing.dialog.ModalDialogBase;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public final class UserDataDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private UserDataDefinitions userDataDefs;
	private Instant time;
	
	private UserDataEditor editor;
	
	private UserDataDialog(App app, Instant time, LinkedHashMap<String, TimelineValue> userData) {
		super(app, "User Data");
		
		this.time = time;
		
		var path = Resources.getUserDataDefinitionsFile().toPath();
		try {
			this.userDataDefs = UserDataDefinitions.fromXml(path);
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
			app.getServices().getUserDataIo().save(this.time, values);
			super.app.getTimelineTablePanel().update(this.time, values);
		} catch (Exception x) {
			MessageDialogs.showError(this, "Failed to save user data", x); 
		}
	}
	
	public static void show(App app, Instant t, LinkedHashMap<String, TimelineValue> userData) { 
		new UserDataDialog(app, t, userData).setVisible(true); }
}
