package astrogeist.ui.swing.component.data.files;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import astrogeist.engine.file.FileRecord;
import astrogeist.engine.resources.Time;
import astrogeist.engine.typesystem.Type;

public final class FilesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private static final short NAME      =             0;
	private static final short TYPE      = NAME      + 1;
	private static final short EXTENSION = TYPE      + 1;
	private static final short SIZE      = EXTENSION + 1;
	private static final short TIMESTAMP = SIZE      + 1;
	private static final short MODIFIED  = TIMESTAMP + 1;

	private final String[] columns = {
		"Name", 
		"Type", 
		"Extension", 
		"Size (KB)",
		"Timestamp",
		"Last Modified" };
	
	private final List<FileRecord> entries = new ArrayList<>();

	@Override public final int getRowCount() { return entries.size(); }
	@Override public final int getColumnCount() { return columns.length; }
	@Override public final String getColumnName(int col) { return columns[col]; }

	@Override public final Object getValueAt(int row, int col) {
		var entry = entries.get(row);
		return switch (col) {
			case NAME      -> entry.getName().toString();
			case TYPE      -> entry.fileType().getFileTypeName();
			case EXTENSION -> entry.getExtension();
			case SIZE      -> entry.sizeKB();
			case TIMESTAMP -> Time.TimeFormatter.format(entry.timestamp());
			case MODIFIED  -> entry.lastModified();
			default -> null;
		};
	}

	public final FileRecord getEntry(int row) {
		if (row >= 0 && row < entries.size()) return entries.get(row);
		return null;
	}

	public final void setFiles(Type.DiskFile fileType, Instant timestamp, List<Path> files) {
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
