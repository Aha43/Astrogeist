package astrogeist.app.dialog.userdata;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Instant;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import astrogeist.app.App;
import astrogeist.app.component.userdata.UserDataEditor;
import astrogeist.app.dialog.message.MessageDialogs;
import astrogeist.app.resources.Resources;
import astrogeist.userdata.UserDataDefinitions;
import astrogeist.userdata.UserDataIo;

public class UserDataDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private App app;
	
	private UserDataDefinitions userDataDefs;
	private Instant t;
	
	private UserDataEditor editor;
	
	private UserDataDialog(App app, Instant t, LinkedHashMap<String, String> userData) {
		super(app.getFrame(), "User Data");
		
		this.app = app;
		
		this.t = t;
		
		setLayout(new BorderLayout());
		
		var path = Resources.getUserDataDefinitionsFile().toPath();
		try {
			this.userDataDefs = UserDataDefinitions.fromXml(path);
			
			this.editor = new UserDataEditor(this.userDataDefs.getUserDataDefinitions(), userData);
			add(this.editor, BorderLayout.CENTER);
			
			createButtons();
			
			pack();
		} catch (Exception x) {
			MessageDialogs.showError(this, x, "Failed to open user data definition file");
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
			UserDataIo.save(this.t, values);
			this.app.getObservationStoreTablePanel().update(t, values);
		} catch (Exception x) {
			MessageDialogs.showError(this, x, "Failed to save user data");
		}
	}
	
	public static void ShowDialog(App app, Instant t, LinkedHashMap<String, String> userData) { 
		new UserDataDialog(app, t, userData).setVisible(true); 
	}
}
