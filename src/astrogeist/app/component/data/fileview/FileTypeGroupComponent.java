package astrogeist.app.component.data.fileview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import astrogeist.app.dialog.files.FileListDialog;
import astrogeist.resources.FileTypeColorMap;
import astrogeist.util.FilesUtil;

public class FileTypeGroupComponent extends JPanel {
    private static final long serialVersionUID = 1L;
	private final String extension;
    private final List<Path> files;
    
    public static FileTypeGroupComponent ofFiles(String extension, List<File> files) {
    	var paths = FilesUtil.filesToPaths(files);
    	return new FileTypeGroupComponent(extension, paths);
    }

    public FileTypeGroupComponent(String extension, List<Path> files) {
        this.extension = extension;
        this.files = files;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Lookup background color
        Color bg = FileTypeColorMap.EXTENSION_COLORS.getOrDefault(
            extension.toLowerCase(),
            Color.WHITE
        );
        setBackground(bg);

        JLabel header = new JLabel(extension.toUpperCase() + " Files", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        add(header, BorderLayout.NORTH);

        JLabel countLabel = new JLabel(String.valueOf(files.size()), SwingConstants.CENTER);
        countLabel.setFont(countLabel.getFont().deriveFont(32f));
        add(countLabel, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FileListDialog.showDialog(extension, files);
            }
        });
    }
}
