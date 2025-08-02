package astrogeist.app.component.data.fileview;

import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JPanel;

import astrogeist.timeline.TimelineUtil;
import astrogeist.timeline.TimelineValue;

public final class FilesPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public FilesPanel() { super(new FlowLayout(FlowLayout.LEFT)); }
	
	public void setData(LinkedHashMap<String, TimelineValue> data) {
		var filePaths = new ArrayList<String>();
		for (var v : TimelineUtil.getOfType(data, "file")) filePaths.add(v.value());
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
