package astrogeist.ui.swing.dialog.timeline.mapping;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import astrogeist.ui.swing.App;
import astrogeist.ui.swing.component.data.timelineview.mapping.TimelineMappingEditor;
import astrogeist.ui.swing.dialog.ModalDialogBase;

public final class TimelineMappingDialog extends ModalDialogBase {
	private static final long serialVersionUID = 1L;
	
	private final TimelineMappingEditor editor = new TimelineMappingEditor(); 
	
	private TimelineMappingDialog(App app) {
		super(app, "Timeline Mapping");
		
		super.add(this.editor, BorderLayout.CENTER);
		makeButtons();
		pack();
	}
	
	private void makeButtons() {
		var buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		var save = new JButton("Save");
		save.addActionListener(e -> save());
		buttonsPanel.add(save);
		super.add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	private void load() { this.editor.load(); }
	
	private void save() { this.editor.save(); }
	
	public static void show(App app) { 
		var dlg = new TimelineMappingDialog(app);
		dlg.load();
		dlg.setVisible(true);
	}
}
