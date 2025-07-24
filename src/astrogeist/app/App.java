package astrogeist.app;

import java.awt.BorderLayout;
import java.awt.Dimension;

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

import astrogeist.app.component.ObservationTablePanel;
import astrogeist.store.ObservationStore;

public final class App {
	
	private final ObservationTablePanel tablePanel = new ObservationTablePanel();
	
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
		var tableScroll = new JScrollPane(tablePanel);
		

		// Left: Tabbed Pane
		var leftTabs = new JTabbedPane();
		leftTabs.setMinimumSize(new Dimension(200, 100));
		leftTabs.addTab("Properties", new JPanel());
		leftTabs.addTab("Issues", new JPanel());
		leftTabs.addTab("Tasks", new JPanel());

		// Split Pane: Left (tabs) + Center (table)
		var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftTabs, tableScroll);
		splitPane.setDividerLocation(250); // Initial size of left pane
		frame.add(splitPane, BorderLayout.CENTER);

		// Bottom: File panel
		var filePanel = new JPanel();
		filePanel.setPreferredSize(new Dimension(100, 120));
		frame.add(filePanel, BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	private JMenuBar createMenuBar() {
		var menuBar = new JMenuBar();

		var file = new JMenu("File");
		var loadItem = new JMenuItem("Load");
		file.add(loadItem);
		loadItem.addActionListener(e -> {
			System.out.println("Load clicked");
			tablePanel.setStore(ObservationStore.createDummyStore());
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

}
