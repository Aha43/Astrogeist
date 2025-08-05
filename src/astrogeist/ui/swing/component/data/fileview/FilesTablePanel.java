package astrogeist.ui.swing.component.data.fileview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import astrogeist.ui.swing.dialog.message.MessageDialogs;

public class FilesTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final DefaultTableModel model;
	private final JTable table;
	
	public FilesTablePanel() {
		super.setLayout(new BorderLayout());
		
		String[] columns = {"Path", "Size (KB)", "Last Modified"};
        this.model = new DefaultTableModel(columns, 0);

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
	
	public void setFiles(List<Path> files) {
		for (var file : files) {
            try {
                var sizeKB = Files.size(file) / 1024;
                var lastMod = Files.getLastModifiedTime(file).toString();
                model.addRow(new Object[]{file.toString(), sizeKB, lastMod});
            } catch (Exception ex) {
                model.addRow(new Object[]{file.toString(), "?", "?"});
            }
        }
	}
	
	private void openFile() {
		var row = table.getSelectedRow();
		if (row >= 0) {
			var path = (String) model.getValueAt(row, 0);
			try {
				java.awt.Desktop.getDesktop().open(Path.of(path).toFile());
			} catch (Exception x) {
				MessageDialogs.showError("Failed to open file: ", x);
			}
		}
	}

	private void openInLocation() {
		var row = table.getSelectedRow();
		if (row >= 0) {
			var path = (String) model.getValueAt(row, 0);
			try {
				Path filePath = Path.of(path);
				java.awt.Desktop.getDesktop().open(filePath.getParent().toFile());
			} catch (Exception x) {
				MessageDialogs.showError("Failed to open location: ", x);
			}
		}
	}

}
