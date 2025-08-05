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

import astrogeist.engine.resources.Resources;
import astrogeist.ui.swing.component.data.fileview.FilesPanel;
import astrogeist.ui.swing.component.data.metadataview.MetadataTablePanel;
import astrogeist.ui.swing.component.data.timelineview.TimelineTablePanel;
import astrogeist.ui.swing.menubar.MenuBarFactory;
import astrogeist.ui.swing.toolbar.ToolBarFactory;

public final class App {
	private JFrame frame = null;
	
	private final MetadataTablePanel metadataPanel = new MetadataTablePanel();
	private final FilesPanel filesPanel = new FilesPanel(this);
	private final TimelineTablePanel timelinePanel = 
		new TimelineTablePanel(this, metadataPanel, filesPanel);
	
	public TimelineTablePanel getTimelineTablePanel() { return this.timelinePanel; }
	
	public void createGUI() {
		this.frame = new JFrame("Astrogeist");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(1200, 800);
		this.frame.setLayout(new BorderLayout());

		this.frame.setJMenuBar(MenuBarFactory.createMenuBar(this));
		
		this.frame.add(ToolBarFactory.createToolBar(this.timelinePanel), BorderLayout.NORTH);

		var timelineScroll = new JScrollPane(this.timelinePanel);

		var leftTabs = new JTabbedPane();
		leftTabs.setMinimumSize(new Dimension(200, 100));
		leftTabs.addTab("Metadata", this.metadataPanel);
		
		var centerTabs = new JTabbedPane();
		centerTabs.addTab("Timeline", timelineScroll);

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
	
	public JFrame getFrame() { return this.frame; }
	
	public void seetingsUpdated() { this.timelinePanel.settingsUpdated(); }
	
	private void addSelectedObservationListener() {
		this.timelinePanel.addSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
		        int selectedRow = this.timelinePanel.getTable().getSelectedRow();
		        if (selectedRow >= 0) {
		            var timestamp = this.timelinePanel.getTableModel().getTimestampAt(selectedRow);
		            var data = this.timelinePanel.getData().snapshotRaw(timestamp);
		            this.metadataPanel.setData(data);
		            this.filesPanel.setData(data);
		        }
		    }
		});
	}

}
