package astrogeist.app.component.fileview;

import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JPanel;

import astrogeist.store.TimelineValue;

public final class ObservationFilesPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public ObservationFilesPanel() { super(new FlowLayout(FlowLayout.LEFT)); }
	
	public void setObservationOld(LinkedHashMap<String, String> observation) {
		var filePaths = new ArrayList<String>();
		
		var serFilePath = observation.get("SerFile");
		if (serFilePath != null) filePaths.add(serFilePath);
		
		setFiles(filePaths);
	}
	
	public void set(List<TimelineValue> values) {
		var filePaths = new ArrayList<String>();
		for (var v : values) {
			if (!v.type().equals("file")) continue;
			filePaths.add(v.value());
		}
		setFiles(filePaths);
	}
	
	public void clear() { setFiles(null); }
	
	private void setFiles(List<String> filePaths) {
        removeAll();
        if (filePaths != null)
        	for (var path : filePaths) this.add(new FileLabel(new File(path)));
        revalidate();
        repaint();
    }

}
