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
	
	private JFrame _frame = null;
	
	private final PropertiesTablePanel _propertiesPanel = new PropertiesTablePanel();
	private final ObservationFilesPanel _filesPanel = new ObservationFilesPanel();
	private final ObservationStoreTablePanel _tablePanel = 
		new ObservationStoreTablePanel(this, _propertiesPanel, _filesPanel);
	
	public void createGUI() {
		_frame = new JFrame("Astrogeist");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setSize(1200, 800);
		_frame.setLayout(new BorderLayout());

		_frame.setJMenuBar(MenuBarFactory.createMenuBar(this));
		
		_frame.add(ToolBarFactory.createToolBar(_tablePanel), BorderLayout.NORTH);

		var tableScroll = new JScrollPane(_tablePanel);

		var leftTabs = new JTabbedPane();
		leftTabs.setMinimumSize(new Dimension(200, 100));
		leftTabs.addTab("Properties", _propertiesPanel);
		leftTabs.addTab("Issues", new JPanel());
		leftTabs.addTab("Tasks", new JPanel());

		// Split Pane: Left (tabs) + Center (table)
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabs, tableScroll);
		splitPane.setDividerLocation(250); // Initial size of left pane
		_frame.add(splitPane, BorderLayout.CENTER);

		// Bottom: File panel
		_filesPanel.setPreferredSize(new Dimension(100, 120));
		_frame.add(_filesPanel, BorderLayout.SOUTH);
		
		addSelectedObservationListener();
		
		URL url = Resources.getLogoUrl(this);
		var icon = new ImageIcon(url).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);;
		_frame.setIconImage(icon);

		_frame.setVisible(true);
	}
	
	public JFrame getFrame() { return _frame; }
	
	public void seetingsUpdated() { _tablePanel.settingsUpdated(); }
	
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
