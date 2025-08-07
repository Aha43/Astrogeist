package astrogeist.ui.swing.component.data.files;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import astrogeist.engine.typesystem.Type;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.files.FilePropertiesDialog;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public class FilesTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final App app;

	private final FilesTableModel model;
	private final JTable table;

	public FilesTablePanel(App app) {
		super.setLayout(new BorderLayout());
		
		this.app = app;

		this.model = new FilesTableModel();
		this.table = new JTable(model);

		var scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(700, 400));
		super.add(scroll, BorderLayout.CENTER);

		var popupMenu = new JPopupMenu();

		var openInLocation = new JMenuItem("Open in Location");
		openInLocation.addActionListener(e -> openInLocation());

		var openFile = new JMenuItem("Open File");
		openFile.addActionListener(e -> openFile());
		
		var properties = new JMenuItem("Properties");
		properties.addActionListener(e -> openPropertiesDialog());

		popupMenu.add(openInLocation);
		popupMenu.add(openFile);
		popupMenu.add(properties);
		
		table.setComponentPopupMenu(popupMenu);
		
		var buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		var openInLocationButton = new JButton("Open in Location");
		openInLocationButton.addActionListener(e -> openInLocation());
		buttons.add(openInLocationButton);
		
		var openFileButton = new JButton("Open");
		openFileButton.addActionListener(e -> openFile());
		buttons.add(openFileButton);
		
		var propertiesButton = new JButton("Properties");
		propertiesButton.addActionListener(e -> openPropertiesDialog());
		buttons.add(propertiesButton);
		
		super.add(buttons, BorderLayout.SOUTH);
	}

	public void setFiles(Type.DiskFile fileType, Instant timestamp, List<Path> files) { 
		model.setFiles(fileType, timestamp, files); }

	private void openFile() {
		var entry = model.getEntry(table.getSelectedRow());
		if (entry != null) {
			try {
				java.awt.Desktop.getDesktop().open(entry.path().toFile());
			} catch (Exception x) {
				MessageDialogs.showError("Failed to open file: ", x);
			}
		} else {
			MessageDialogs.showInfo(this, NO_FILE_SELECTED_MESSAGE);
		}
		
	}

	private void openInLocation() {
		var entry = model.getEntry(table.getSelectedRow());
		if (entry != null) {
			try {
				java.awt.Desktop.getDesktop().open(entry.path().getParent().toFile());
			} catch (Exception x) {
				MessageDialogs.showError("Failed to open location: ", x);
			}
		} else {
			MessageDialogs.showInfo(this, NO_FILE_SELECTED_MESSAGE);
		}
	}
	
	private void openPropertiesDialog() {
		var entry = model.getEntry(table.getSelectedRow());
		if (entry != null) {
			FilePropertiesDialog.show(app, entry);
		} else {
			MessageDialogs.showInfo(this, NO_FILE_SELECTED_MESSAGE);
		}
	}
	
	private final static String NO_FILE_SELECTED_MESSAGE = "No file selected"; 
}
