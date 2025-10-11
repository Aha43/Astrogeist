package astrogeist.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import astrogeist.engine.abstraction.ServiceProvider;
import astrogeist.engine.abstraction.selection.SnapshotSelectionService;
import astrogeist.engine.abstraction.timeline.Timeline;
import astrogeist.engine.abstraction.timeline.TimelineNames;
import astrogeist.engine.abstraction.timeline.TimelineValuePool;
import astrogeist.engine.resources.Resources;
import astrogeist.engine.userdata.UserDataIo;
import astrogeist.ui.swing.component.data.files.FilesTypeGroupComponentPanel;
import astrogeist.ui.swing.component.data.metadata.MetadataTablePanel;
import astrogeist.ui.swing.component.data.timeline.TimelineTablePanel;
import astrogeist.ui.swing.component.data.timeline.filtering.FilteredTimelineViewTablePanel;
import astrogeist.ui.swing.menubar.MenuBarFactory;
import astrogeist.ui.swing.scanning.ShowScanningDialogAction;
import astrogeist.ui.swing.toolbar.ToolBarFactory;

public final class App {
	private final ServiceProvider services = new AstrogeistServiceProvider();
	
	private JFrame frame = null;
	
	private final MetadataTablePanel metadataPanel = new MetadataTablePanel(
		this.services.get(SnapshotSelectionService.class));
	
	private final FilesTypeGroupComponentPanel filesPanel = new FilesTypeGroupComponentPanel(
		this,
		this.services.get(SnapshotSelectionService.class));
	
	private final TimelineTablePanel timelinePanel = new TimelineTablePanel(
		this,
		this.services.get(UserDataIo.class),
		this.services.get(TimelineNames.class), 
		this.services.get(SnapshotSelectionService.class));
	
	private final FilteredTimelineViewTablePanel searchPanel = new FilteredTimelineViewTablePanel(
		this,
		this.services.get(TimelineNames.class),
		this.services.get(SnapshotSelectionService.class));
	
	// TODO: Find way to refactor so not expose internal components.
	public final TimelineTablePanel getTimelinePanel() { return this.timelinePanel; }
	
	// TODO: Find way to refactor so not expose internal components.
	public final FilteredTimelineViewTablePanel getSearchPanel() { return this.searchPanel; }
	
	// Actions
	public final Action ScanAction = new ShowScanningDialogAction(
		this,	
		this.services.get(Timeline.class),
		this.services.get(TimelineValuePool.class));
	
	public final void createGUI() {
		var title = "Astrogeist";
		
		if (Resources.isDevelopmentMode()) title += " (development)";
		
		this.frame = new JFrame(title);
		
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(1200, 800);
		this.frame.setLayout(new BorderLayout());

		this.frame.setJMenuBar(MenuBarFactory.createMenuBar(
			this,
			this.services.get(TimelineNames.class)));
		
		this.frame.add(ToolBarFactory.createToolBar(this), BorderLayout.NORTH);

		var timelineScroll = new JScrollPane(this.timelinePanel);
		var timelineViewScroll = new JScrollPane(this.searchPanel);

		var leftTabs = new JTabbedPane();
		leftTabs.setMinimumSize(new Dimension(200, 100));
		leftTabs.addTab("Metadata", this.metadataPanel);
		
		var centerTabs = new JTabbedPane();
		centerTabs.addTab("Timeline", timelineScroll);
		centerTabs.addTab("Filtered", timelineViewScroll);

		// Split Pane: Left (tabs) + Center (table)
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabs, centerTabs);
		splitPane.setDividerLocation(250); // Initial size of left pane
		this.frame.add(splitPane, BorderLayout.CENTER);

		// Bottom: File panel
		var southTabs = new JTabbedPane();
		southTabs.addTab("Files", filesPanel);
		this.filesPanel.setPreferredSize(new Dimension(100, 120));
		this.frame.add(southTabs, BorderLayout.SOUTH);
		
		URL url = Resources.getLogoUrl(this);
		var icon = new ImageIcon(url).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);;
		this.frame.setIconImage(icon);

		this.frame.setVisible(true);
	}
	
	public final JFrame getFrame() { return this.frame; }
	
	public final void seetingsUpdated() { 
		this.timelinePanel.settingsUpdated();
		this.searchPanel.settingsUpdated();
	}

}
