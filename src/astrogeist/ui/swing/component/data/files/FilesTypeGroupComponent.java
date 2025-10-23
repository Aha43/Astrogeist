package astrogeist.ui.swing.component.data.files;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import astrogeist.common.FilesUtil;
import astrogeist.engine.resources.FileTypeColorMap;
import astrogeist.engine.typesystem.Type;
import astrogeist.ui.swing.App;
import astrogeist.ui.swing.dialog.files.FileListDialog;

public class FilesTypeGroupComponent extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final Type.DiskFile fileType;
    
    private FilesTypeGroupComponent(App app, Type.DiskFile fileType, Instant timestamp, List<Path> files) {
        this.fileType = fileType;
        
    	setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Lookup background color
        Color bg = FileTypeColorMap.EXTENSION_COLORS.getOrDefault(fileType, Color.WHITE);
        setBackground(bg);

        JLabel header = new JLabel(" " + fileType.getFileTypeName() + " Files ", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        add(header, BorderLayout.NORTH);

        JLabel countLabel = new JLabel(String.valueOf(files.size()), SwingConstants.CENTER);
        countLabel.setFont(countLabel.getFont().deriveFont(32f));
        add(countLabel, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FileListDialog.show(app, fileType, timestamp, files); }
        });
    }
    
    public Type.DiskFile getFileType() { return this.fileType; }
    
    public static FilesTypeGroupComponent ofFiles(App app, Type.DiskFile fileType, Instant timestamp, List<File> files) {
    	var paths = FilesUtil.filesToPaths(files);
    	return new FilesTypeGroupComponent(app, fileType, timestamp, paths);
    }
}
