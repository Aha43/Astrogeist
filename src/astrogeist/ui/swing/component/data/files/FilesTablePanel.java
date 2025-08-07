package astrogeist.ui.swing.component.data.files;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import astrogeist.engine.typesystem.Type;
import astrogeist.ui.swing.dialog.message.MessageDialogs;

public class FilesTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final FilesTableModel model;
	private final JTable table;

	public FilesTablePanel() {
		super.setLayout(new BorderLayout());

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

		popupMenu.add(openInLocation);
		popupMenu.add(openFile);

		table.setComponentPopupMenu(popupMenu);
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
		}
	}
}
