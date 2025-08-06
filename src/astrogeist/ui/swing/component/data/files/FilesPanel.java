package astrogeist.ui.swing.component.data.files;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JPanel;

import astrogeist.engine.timeline.TimelineUtil;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;
import astrogeist.engine.util.FilesUtil;
import astrogeist.ui.swing.App;

public final class FilesPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private App app;
	
	public FilesPanel(App app) { super.setLayout(new FlowLayout(FlowLayout.LEFT)); this.app = app; }
	
	public void setData(LinkedHashMap<String, TimelineValue> data) {
		var filePaths = new ArrayList<String>();
		for (var v : TimelineUtil.getOfType(data, Type.DiskFile())) filePaths.add(v.value());
		setFiles(filePaths);
	}
	
	public void clear() { setFiles(null); }
	
	private void setFiles(List<String> filePaths) {
        super.removeAll();
        
        if (filePaths == null || filePaths.isEmpty()) return;
        
        var files = FilesUtil.stringsToFiles(filePaths);
        var grouped = FilesUtil.groupByExtension(files);
        for (var group : grouped.entrySet()) {
        	var comp = FileTypeGroupComponent.ofFiles(app, group.getKey(), group.getValue());
        	super.add(comp);
        }
        
        revalidate();
        repaint();
    }

}
