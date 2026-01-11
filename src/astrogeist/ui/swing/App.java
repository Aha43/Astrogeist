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

import aha.common.abstraction.io.appdata.AppDataManager;
import astrogeist.engine.abstraction.ServiceProvider;
import astrogeist.engine.abstraction.TimelineManager;
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
	private final AstrogeistServiceProvider services =
		new AstrogeistServiceProvider();
	
	private final JFrame frame = new JFrame("Astrogeist");
	
	private final MetadataTablePanel metadataPanel; 
	private final FilesTypeGroupComponentPanel filesPanel; 
	private final TimelineTablePanel timelinePanel;
	private final FilteredTimelineViewTablePanel searchPanel;
	
	// Actions
	public final Action ScanAction; 
	
	public App() {
		this.metadataPanel = new MetadataTablePanel(
			this.services.get(SnapshotSelectionService.class));
		
		this.filesPanel = new FilesTypeGroupComponentPanel(
			this,
			this.services.get(SnapshotSelectionService.class));
		
		this.timelinePanel = new TimelineTablePanel(
			this,
			this.services.get(AppDataManager.class),
			this.services.get(UserDataIo.class),
			this.services.get(TimelineNames.class), 
			this.services.get(SnapshotSelectionService.class));
		
		this.searchPanel = new FilteredTimelineViewTablePanel(
			this,
			this.services.get(AppDataManager.class),
			this.services.get(TimelineNames.class),
			this.services.get(SnapshotSelectionService.class));
		
		var timelineManager = 
			new DefaultTimelineManager(searchPanel, timelinePanel);
		this.services.singleton(timelineManager, TimelineManager.class);
			
		this.ScanAction = new ShowScanningDialogAction(
			this,
			this.services.get(AppDataManager.class),
			this.services.get(Timeline.class),
			this.services.get(TimelineValuePool.class));
	}
	
	public final ServiceProvider serviceProvider() { return this.services; }
	
	/**
	 * <p>
	 *   Convenient method to get service from
	 *   {@link #serviceProvider() service provider}.
	 * </p>
	 * @param <T>   Type of service to get.
	 * @param clazz Class of service to get.
	 * @return the service.
	 */
	public final <T> T service(Class<? extends T> clazz) { 
		return this.services.get(clazz); }
	
	public final void createGUI() {
		if (Resources.isDevelopmentMode())
			this.frame.setTitle(this.frame.getTitle() + " (development)");
			
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(1200, 800);
		this.frame.setLayout(new BorderLayout());

		this.frame.setJMenuBar(MenuBarFactory.createMenuBar(
			this,
			this.services.get(AppDataManager.class),
			this.services.get(TimelineNames.class),
			this.services.get(SnapshotSelectionService.class)));
		
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
		var splitPane =
			new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabs, centerTabs);
		splitPane.setDividerLocation(250); // Initial size of left pane
		this.frame.add(splitPane, BorderLayout.CENTER);

		// Bottom: File panel
		var southTabs = new JTabbedPane();
		southTabs.addTab("Files", filesPanel);
		this.filesPanel.setPreferredSize(new Dimension(100, 120));
		this.frame.add(southTabs, BorderLayout.SOUTH);
		
		URL url = Resources.getLogoUrl(this);
		var icon = new ImageIcon(url).getImage().getScaledInstance(16, 16,
			Image.SCALE_SMOOTH);
		this.frame.setIconImage(icon);

		this.frame.setVisible(true);
	}
	
	public final JFrame getFrame() { return this.frame; }
	
	public final void seetingsUpdated() { 
		this.timelinePanel.settingsUpdated();
		this.searchPanel.settingsUpdated();
	}

}
