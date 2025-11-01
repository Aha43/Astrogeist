package astrogeist.ui.swing.component.data.files;

import java.awt.FlowLayout;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import aha.common.FilesUtil;
import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.timeline.TimelineSnapshotUtil;
import astrogeist.engine.timeline.TimelineValue;
import astrogeist.engine.typesystem.Type;
import astrogeist.ui.swing.App;

public final class FilesTypeGroupComponentPanel extends JPanel implements SnapshotListener {
	private static final long serialVersionUID = 1L;
	
	private App app;
	
	public FilesTypeGroupComponentPanel(
		App app,
		SnapshotSelectionService snapshotSelectionService) { 
		
		super.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		this.app = app;
		snapshotSelectionService.addListener(this);
	}
	
	public final void setData(Instant timestamp, Map<String, TimelineValue> data) {
		var filePaths = new ArrayList<String>();
		for (var v : TimelineSnapshotUtil.getOfType(data, Type.DiskFile())) filePaths.add(v.value());
		setFiles(timestamp, filePaths);
	}
	
	public final void clear() { super.removeAll(); revalidate(); repaint(); }
	
	private final void setFiles(Instant timestamp, List<String> filePaths) {
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

	@Override public final void onSnapshotSelected(Instant timestamp, Map<String, TimelineValue> snapshot) {
		this.setData(timestamp, snapshot); }

	@Override public final void onSelectionCleared() { this.clear(); }

}
