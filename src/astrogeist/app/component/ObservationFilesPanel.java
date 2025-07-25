package astrogeist.app.component;

import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

public final class ObservationFilesPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public ObservationFilesPanel() { super(new FlowLayout(FlowLayout.LEFT)); }
	
	public void setObservation(Map<String, String> observation) {
		var filePaths = new ArrayList<String>();
		
		var serFilePath = observation.get("movie:ser-file");
		if (serFilePath != null) filePaths.add(serFilePath);
		
		setFiles(filePaths);
	}
	
	private void setFiles(List<String> filePaths) {
        this.removeAll();

        for (String path : filePaths) this.add(new FileLabel(new File(path)));

        this.revalidate();
        this.repaint();
    }

}
