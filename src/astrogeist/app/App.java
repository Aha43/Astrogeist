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

import astrogeist.app.component.ObservationFilesPanel;
import astrogeist.app.component.ObservationTablePanel;
import astrogeist.app.component.PropertiesTablePanel;
import astrogeist.app.menubar.MenuBarFactory;
import astrogeist.app.toolbar.ToolBarFactory;

public final class App {
	
	private final ObservationTablePanel _tablePanel = new ObservationTablePanel();
	private final PropertiesTablePanel _propertiesPanel = new PropertiesTablePanel();
	private final ObservationFilesPanel _filesPanel = new ObservationFilesPanel();
	
	public void createGUI() {
		var frame = new JFrame("Astrogeist");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 800);
		frame.setLayout(new BorderLayout());

		// Menu Bar
		frame.setJMenuBar(MenuBarFactory.createMenuBar(_tablePanel));

		// Toolbar
		var toolBar = ToolBarFactory.createToolBar();
		frame.add(toolBar, BorderLayout.NORTH);

		// Table (Center)
		var tableScroll = new JScrollPane(_tablePanel);

		// Left: Tabbed Pane
		var leftTabs = new JTabbedPane();
		leftTabs.setMinimumSize(new Dimension(200, 100));
		leftTabs.addTab("Properties", _propertiesPanel);
		leftTabs.addTab("Issues", new JPanel());
		leftTabs.addTab("Tasks", new JPanel());

		// Split Pane: Left (tabs) + Center (table)
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabs, tableScroll);
		splitPane.setDividerLocation(250); // Initial size of left pane
		frame.add(splitPane, BorderLayout.CENTER);

		// Bottom: File panel
		_filesPanel.setPreferredSize(new Dimension(100, 120));
		frame.add(_filesPanel, BorderLayout.SOUTH);
		
		addSelectedObservationListener();
		
		URL url = getClass().getResource("/astrogeist/app/logo.png");
		System.out.println("Icon URL: " + url);
		var icon = new ImageIcon(url).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);;
		frame.setIconImage(icon);

		frame.setVisible(true);
	}
	
	private void addSelectedObservationListener() {
		_tablePanel.addSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
		        int selectedRow = _tablePanel.getTable().getSelectedRow();
		        if (selectedRow >= 0) {
		            Instant timestamp = _tablePanel.getTableModel().getTimestampAt(selectedRow);
		            Map<String, String> observation = _tablePanel.getStore().snapshot(timestamp);
		            _propertiesPanel.setProperties(observation);
		            _filesPanel.setObservation(observation);
		        }
		    }
		});
	}

}
