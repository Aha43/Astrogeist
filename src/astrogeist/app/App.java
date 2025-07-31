package astrogeist.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.time.Instant;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import astrogeist.app.component.fileview.ObservationFilesPanel;
import astrogeist.app.component.observationstoreview.ObservationStoreTablePanel;
import astrogeist.app.component.propertiesview.PropertiesTablePanel;
import astrogeist.app.menubar.MenuBarFactory;
import astrogeist.app.resources.Resources;
import astrogeist.app.toolbar.ToolBarFactory;

public final class App {
	
	private JFrame frame = null;
	
	private final PropertiesTablePanel propertiesPanel = new PropertiesTablePanel();
	private final ObservationFilesPanel filesPanel = new ObservationFilesPanel();
	private final ObservationStoreTablePanel tablePanel = 
		new ObservationStoreTablePanel(this, propertiesPanel, filesPanel);
	
	public void createGUI() {
		this.frame = new JFrame("Astrogeist");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(1200, 800);
		this.frame.setLayout(new BorderLayout());

		this.frame.setJMenuBar(MenuBarFactory.createMenuBar(this));
		
		this.frame.add(ToolBarFactory.createToolBar(this.tablePanel), BorderLayout.NORTH);

		var tableScroll = new JScrollPane(this.tablePanel);

		var leftTabs = new JTabbedPane();
		leftTabs.setMinimumSize(new Dimension(200, 100));
		leftTabs.addTab("Properties", this.propertiesPanel);
		leftTabs.addTab("Issues", new JPanel());
		leftTabs.addTab("Tasks", new JPanel());

		// Split Pane: Left (tabs) + Center (table)
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabs, tableScroll);
		splitPane.setDividerLocation(250); // Initial size of left pane
		this.frame.add(splitPane, BorderLayout.CENTER);

		// Bottom: File panel
		this.filesPanel.setPreferredSize(new Dimension(100, 120));
		this.frame.add(this.filesPanel, BorderLayout.SOUTH);
		
		addSelectedObservationListener();
		
		URL url = Resources.getLogoUrl(this);
		var icon = new ImageIcon(url).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);;
		this.frame.setIconImage(icon);

		this.frame.setVisible(true);
	}
	
	public JFrame getFrame() { return this.frame; }
	
	public void seetingsUpdated() { this.tablePanel.settingsUpdated(); }
	
	private void addSelectedObservationListener() {
		this.tablePanel.addSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
		        int selectedRow = this.tablePanel.getTable().getSelectedRow();
		        if (selectedRow >= 0) {
		            Instant timestamp = this.tablePanel.getTableModel().getTimestampAt(selectedRow);
		            Map<String, String> observation = this.tablePanel.getStore().snapshot(timestamp);
		            this.propertiesPanel.setProperties(observation);
		            this.filesPanel.setObservation(observation);
		        }
		    }
		});
	}

}
