package astrogeist.ui.swing.component.data.files;

import java.awt.FlowLayout;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import astrogeist.engine.timeline.TimelineUtil;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;
import astrogeist.engine.util.FilesUtil;
import astrogeist.ui.swing.App;

public final class FilesTypeGroupComponentPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private App app;
	
	public FilesTypeGroupComponentPanel(App app) { super.setLayout(new FlowLayout(FlowLayout.LEFT)); this.app = app; }
	
	public void setData(Instant timestamp, Map<String, TimelineValue> data) {
		var filePaths = new ArrayList<String>();
		for (var v : TimelineUtil.getOfType(data, Type.DiskFile())) filePaths.add(v.value());
		setFiles(timestamp, filePaths);
	}
	
	public void clear() { super.removeAll(); revalidate(); repaint(); }
	
	private void setFiles(Instant timestamp, List<String> filePaths) {
        super.removeAll();
        
        if (filePaths == null || filePaths.isEmpty()) return;
        
        var files = FilesUtil.stringsToFiles(filePaths);
        var grouped = FilesUtil.groupByExtension(files);
        for (var group : grouped.entrySet()) {
        	var comp = FilesTypeGroupComponent.ofFiles(app, group.getKey(), timestamp, group.getValue());
        	super.add(comp);
        }
        
        revalidate();
        repaint();
    }

}
