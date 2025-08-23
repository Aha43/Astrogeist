package astrogeist.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import astrogeist.engine.Services;
import astrogeist.engine.resources.Resources;
import astrogeist.ui.swing.component.data.files.FilesTypeGroupComponentPanel;
import astrogeist.ui.swing.component.data.metadata.MetadataTablePanel;
import astrogeist.ui.swing.component.data.timeline.TimelineTablePanel;
import astrogeist.ui.swing.component.data.timeline.filtering.FilteredTimelineViewTablePanel;
import astrogeist.ui.swing.menubar.MenuBarFactory;
import astrogeist.ui.swing.toolbar.ToolBarFactory;

public final class App {
	private final Services services = new Services();
	
	private JFrame frame = null;
	
	private final MetadataTablePanel metadataPanel = new MetadataTablePanel();
	
	private final FilesTypeGroupComponentPanel filesPanel = new FilesTypeGroupComponentPanel(this);
	
	private final TimelineTablePanel timelinePanel = new TimelineTablePanel(this);
	
	private final FilteredTimelineViewTablePanel searchPanel = new FilteredTimelineViewTablePanel(this);
	
	public App() {}
	
	public final Services getServices() { return this.services; }
	
	public final MetadataTablePanel getMetadataTablePanel() { return this.metadataPanel; }
	
	public final FilesTypeGroupComponentPanel getFilesPanel() { return this.filesPanel; }
	
	public final TimelineTablePanel getTimelinePanel() { return this.timelinePanel; }
	
	public final FilteredTimelineViewTablePanel getSearchPanel() { return this.searchPanel; }
	
	public final void createGUI() {
		var title = "Astrogeist";
		
		if (Resources.isDevelopmentMode()) title += " (development)";
		
		this.frame = new JFrame(title);
		
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(1200, 800);
		this.frame.setLayout(new BorderLayout());

		this.frame.setJMenuBar(MenuBarFactory.createMenuBar(this));
		
		this.frame.add(ToolBarFactory.createToolBar(this), BorderLayout.NORTH);

		var timelineScroll = new JScrollPane(this.timelinePanel);
		var timelineViewScroll = new JScrollPane(this.searchPanel);

		var leftTabs = new JTabbedPane();
		leftTabs.setMinimumSize(new Dimension(200, 100));
		leftTabs.addTab("Metadata", this.metadataPanel);
		
		var centerTabs = new JTabbedPane();
		centerTabs.addTab("Timeline", timelineScroll);
		centerTabs.addTab("Filtering", timelineViewScroll);

		// Split Pane: Left (tabs) + Center (table)
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabs, centerTabs);
		splitPane.setDividerLocation(250); // Initial size of left pane
		this.frame.add(splitPane, BorderLayout.CENTER);

		// Bottom: File panel
		var southTabs = new JTabbedPane();
		southTabs.addTab("Files", filesPanel);
		this.filesPanel.setPreferredSize(new Dimension(100, 120));
		this.frame.add(southTabs, BorderLayout.SOUTH);
		
		addSelectedObservationListener();
		
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
	
	private final void addSelectedObservationListener() {
		this.timelinePanel.addSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
		        int selectedRow = this.timelinePanel.getSelectedRow();
		        if (selectedRow >= 0) {
		        	var timestamp = this.timelinePanel.getSelectedTimestamp();
		        	var snapshot = this.timelinePanel.getSelectedSnapshot();
		            this.metadataPanel.setData(snapshot);
		            this.filesPanel.setData(timestamp, snapshot);
		        }
		    }
		});
		
		this.searchPanel.addSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
		        int selectedRow = this.searchPanel.getSelectedRow();
		        if (selectedRow >= 0) {
		        	var timestamp = this.searchPanel.getSelectedTimestamp();
		        	var snapshot = this.searchPanel.getSelectedSnapshot();
		            this.metadataPanel.setData(snapshot);
		            this.filesPanel.setData(timestamp, snapshot);
		        }
		    }
		});
	}

}
