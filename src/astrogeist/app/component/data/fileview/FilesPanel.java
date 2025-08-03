package astrogeist.app.component.data.fileview;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JPanel;

import astrogeist.timeline.TimelineUtil;
import astrogeist.timeline.TimelineValue;
import astrogeist.util.FilesUtil;

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
        
        if (filePaths == null || filePaths.isEmpty()) return;
        
        var files = FilesUtil.stringsToFiles(filePaths);
        var grouped = FilesUtil.groupByExtension(files);
        for (var group : grouped.entrySet()) {
        	var comp = FileTypeGroupComponent.ofFiles(group.getKey(), group.getValue());
        	this.add(comp);
        }
        
        revalidate();
        repaint();
    }

}
