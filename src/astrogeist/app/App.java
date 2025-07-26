package astrogeist.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import java.time.Instant;
import java.util.Map;

import javax.swing.ImageIcon;
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
import astrogeist.app.dialog.AboutDialog;
import astrogeist.app.dialog.settings.SettingsDialog;
import astrogeist.scanner.CompositeScanner;
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
		
		URL url = getClass().getResource("/astrogeist/app/logo.png");
		System.out.println("Icon URL: " + url);
		var icon = new ImageIcon(url).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);;
		frame.setIconImage(icon);

		frame.setVisible(true);
	}

	private JMenuBar createMenuBar() {
		var menuBar = new JMenuBar();

		var file = new JMenu("File");
		var loadItem = new JMenuItem("Load");
		file.add(loadItem);
		loadItem.addActionListener(e -> {
			
			// Needs to be refactored...
			var scanner = new CompositeScanner();
			var store = new ObservationStore();
			scanner.scan(store);
			
			_tablePanel.setStore(store);
		});
		
		var settingsItem = new JMenuItem("Settings");
		file.add(settingsItem);
		settingsItem.addActionListener(e -> {
			SettingsDialog dialog = new SettingsDialog(null);
			dialog.setVisible(true);
		});
		
		file.add(new JMenuItem("Exit"));

		var view = new JMenu("View");
		var tools = new JMenu("Tools");
		var help = new JMenu("Help");
		
		var aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(e -> {
		    var dialog = new AboutDialog(null); // Replace with your main window
		    dialog.setVisible(true);
		});
		help.add(aboutItem);

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
