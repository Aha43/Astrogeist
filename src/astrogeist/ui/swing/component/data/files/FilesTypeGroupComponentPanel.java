package astrogeist.ui.swing.component.data.files;

import static aha.common.util.FilesUtil.groupByExtension;
import static aha.common.util.FilesUtil.stringsToFiles;
import static astrogeist.ui.swing.component.data.files.FilesTypeGroupComponent.ofFiles;

import java.awt.FlowLayout;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import astrogeist.engine.abstraction.selection.SnapshotListener;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.timeline.Snapshot;
import astrogeist.engine.typesystem.Type;
import astrogeist.ui.swing.App;

/**
 * <p>
 *   Panel that shows the files associated with the current selected snapshot.
 * </p>
 */
public final class FilesTypeGroupComponentPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public FilesTypeGroupComponentPanel(App app, SnapshotSelectionService sss) { 
		super.setLayout(new FlowLayout(FlowLayout.LEFT)); 
		sss.addListener(new SnapshotListener() {
			@Override public void onSnapshotSelected(Instant t,
				Snapshot sh) { setData(app, t, sh); }
			@Override public void onSelectionCleared() { 
				removeAll(); revalidate(); repaint(); }
		});
	}
	
	private final void setData(App a, Instant t, Snapshot s) {
		var filePaths = new ArrayList<String>();
		for (var v : s.getOfType(Type.DiskFile())) 
			filePaths.add(v.value());
		setFiles(a, t, filePaths);
	}
	
	private final void setFiles(App a, Instant t, List<String> filePaths) {
        super.removeAll();
        
        if (filePaths == null || filePaths.isEmpty()) return;
        
        var files = stringsToFiles(filePaths);
        var grouped = groupByExtension(files);
        for (var group : grouped.entrySet()) {
        	var comp = ofFiles(a, group.getKey(), t, group.getValue());
        	super.add(comp);
        }
        
        revalidate();
        repaint();
    }

}
