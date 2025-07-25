package astrogeist.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.time.Instant;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import astrogeist.app.component.ObservationFilesPanel;
import astrogeist.app.component.ObservationTablePanel;
import astrogeist.app.component.PropertiesTablePanel;
import astrogeist.app.dialog.ConfigDialog;
import astrogeist.scanner.sharpcap.SharpCapScanner;
import astrogeist.store.ObservationStore;

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
		frame.setJMenuBar(createMenuBar());

		// Toolbar
		var toolBar = createToolBar();
		frame.add(toolBar, BorderLayout.NORTH);

		// Table (Center)
		// Replace with your model later
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

		frame.setVisible(true);
	}

	private JMenuBar createMenuBar() {
		var menuBar = new JMenuBar();

		var file = new JMenu("File");
		var loadItem = new JMenuItem("Load");
		file.add(loadItem);
		loadItem.addActionListener(e -> {
			
			// Needs to be refactored...
			var path = "/Volumes/Extreme SSD/SharpCap";
			var scanner = new SharpCapScanner();
			var store = new ObservationStore();
			scanner.scan(store, new File(path));
			
			_tablePanel.setStore(store);
		});
		
		var settingsItem = new JMenuItem("Settings");
		file.add(settingsItem);
		settingsItem.addActionListener(e -> {
			ConfigDialog dialog = new ConfigDialog(null);
			dialog.setVisible(true);
		});
		
		file.add(new JMenuItem("Exit"));

		var view = new JMenu("View");
		var tools = new JMenu("Tools");
		var help = new JMenu("Help");

		menuBar.add(file);
		menuBar.add(view);
		menuBar.add(tools);
		menuBar.add(help);

		return menuBar;
	}

	private JToolBar createToolBar() {
		var toolBar = new JToolBar();

		toolBar.add(new JButton("Rescan"));
		toolBar.add(new JButton("Export"));
		toolBar.addSeparator();
		toolBar.add(new JLabel("Filter: "));
		toolBar.add(new JTextField(10));

		return toolBar;
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
