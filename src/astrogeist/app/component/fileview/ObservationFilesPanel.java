package astrogeist.app.component.fileview;

import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JPanel;

public final class ObservationFilesPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public ObservationFilesPanel() { super(new FlowLayout(FlowLayout.LEFT)); }
	
	public void setObservation(LinkedHashMap<String, String> observation) {
		var filePaths = new ArrayList<String>();
		
		var serFilePath = observation.get("SerFile");
		if (serFilePath != null) filePaths.add(serFilePath);
		
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
