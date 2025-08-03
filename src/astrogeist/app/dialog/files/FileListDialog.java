package astrogeist.app.dialog.files;

import java.awt.Dimension;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FileListDialog {
    public static void showDialog(String extension, List<Path> files) {
        String[] columns = {"Path", "Size (KB)", "Last Modified"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Path file : files) {
            try {
                long sizeKB = Files.size(file) / 1024;
                String lastMod = Files.getLastModifiedTime(file).toString();
                model.addRow(new Object[]{file.toString(), sizeKB, lastMod});
            } catch (Exception ex) {
                model.addRow(new Object[]{file.toString(), "?", "?"});
            }
        }

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(700, 400));

        JOptionPane.showMessageDialog(
            null,
            scroll,
            extension.toUpperCase() + " Files",
            JOptionPane.PLAIN_MESSAGE
        );
    }
}

