package astrogeist.ui.swing.component.data.files;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import astrogeist.engine.file.FileRecord;

public class FilePropertiesPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final FilePropertiesTableModel model = new FilePropertiesTableModel();
	private final JTable table = new JTable(model);

	public FilePropertiesPanel(FileRecord file) {
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		var scroll = new JScrollPane(table);
		add(scroll, BorderLayout.CENTER);

		model.setProperties(new String[][]{
			{"Type", file.fileType().getFileTypeName()},
			{"Name", file.getName()},
			{"Full Name", file.path().getFileName().toString()},
			{"Path", file.path().toString()},
			{"Size (KB)", Long.toString(file.sizeKB())},
			{"Timestamp", file.timestamp().toString()},
			{"Last Modified", file.lastModified()}
		});

		// Future button bar for actions
		//var actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		//actionBar.add(new JButton("Open"));      // placeholder
		//actionBar.add(new JButton("Delete"));    // future
		//actionBar.add(new JButton("Rename"));    // future
		//add(actionBar, BorderLayout.SOUTH);
	}
}
