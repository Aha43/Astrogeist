package astrogeist.ui.swing.component.data.files;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.file.FileRecord;
import astrogeist.engine.typesystem.Type;

public class FilesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private static final short NAME      =             0;
	private static final short TYPE      = NAME      + 1;
	private static final short EXTENSION = TYPE      + 1;
	private static final short SIZE      = EXTENSION + 1;
	private static final short MODIFIED  = SIZE      + 1;

	private final String[] columns = {
		"Name", 
		"Type", 
		"Extension", 
		"Size (KB)", 
		"Last Modified" };
	
	private final List<FileRecord> entries = new ArrayList<>();

	@Override public int getRowCount() { return entries.size(); }
	@Override public int getColumnCount() { return columns.length; }
	@Override public String getColumnName(int col) { return columns[col]; }

	@Override public Object getValueAt(int row, int col) {
		var entry = entries.get(row);
		return switch (col) {
			case NAME      -> entry.getName().toString();
			case TYPE      -> entry.fileType().getFileTypeName();
			case EXTENSION -> entry.getExtension();
			case SIZE      -> entry.sizeKB();
			case MODIFIED  -> entry.lastModified();
			default -> null;
		};
	}

	public FileRecord getEntry(int row) {
		if (row >= 0 && row < entries.size()) return entries.get(row);
		return null;
	}

	public void setFiles(Type.DiskFile fileType, Instant timestamp, List<Path> files) {
		entries.clear();
		for (Path path : files) {
			try {
				var size = Files.size(path) / 1024;
				var lastModified = Files.getLastModifiedTime(path).toString();
				entries.add(new FileRecord(fileType, path, size, timestamp, lastModified));
			} catch (Exception e) {
				entries.add(new FileRecord(fileType, path, -1, timestamp, "?"));
			}
		}
		fireTableDataChanged();
	}
	
}

