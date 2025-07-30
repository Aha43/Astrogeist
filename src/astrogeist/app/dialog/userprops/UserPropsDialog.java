package astrogeist.app.dialog.userprops;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JDialog;

import astrogeist.app.App;
import astrogeist.app.component.userprops.UserPropsEditor;
import astrogeist.app.dialog.message.MessageDialogs;
import astrogeist.app.resources.Resources;
import astrogeist.userprops.UserPropDefs;

public class UserPropsDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private UserPropDefs _propDefs;
	
	private UserPropsDialog(App app) {
		super(app.getFrame(), "User Properties");
		
		setLayout(new BorderLayout());
		
		var path = Resources.getUserPropsFile().toPath();
		try {
			_propDefs = UserPropDefs.fromXml(path);
			
			var editor = new UserPropsEditor(_propDefs.getProperties(), new HashMap<String, String>());
			add(editor, BorderLayout.CENTER);
			pack();
		} catch (Exception x) {
			MessageDialogs.showError(this,"Failed to open sidecar file: " + path);
		}
	}
	
	public static void ShowDialog(App app) { new UserPropsDialog(app).setVisible(true); }
}
