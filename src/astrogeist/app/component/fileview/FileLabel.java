package astrogeist.app.component.fileview;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public final class FileLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	private final File _file;
	
	public FileLabel(File file) {
		super(file.getName());
		
		_file = file;
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setOpaque(true);
        setBackground(new Color(240, 240, 240));
        setToolTipText(file.getAbsolutePath()); // full path as tooltip
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addMouseListener();
        setEnabled(true);
        setVisible(true);
	}
	
	private void addMouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
		    public void mouseEntered(MouseEvent e) { setBackground(new Color(220, 220, 255)); }
			@Override
		    public void mouseExited(MouseEvent e) { setBackground(new Color(240, 240, 240)); }
		    @Override
		    public void mousePressed(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }
		    @Override
		    public void mouseReleased(MouseEvent e) { if (e.isPopupTrigger()) showPopup(e); }

		    private void showPopup(MouseEvent e) {
		        JPopupMenu menu = new JPopupMenu();

		        //JMenuItem openTerminal = new JMenuItem("Open in Terminal");
		        //openTerminal.addActionListener(ev -> openInTerminal(file));

		        JMenuItem showInExplorer = new JMenuItem("Show in File Browser");
		        showInExplorer.addActionListener(ev -> openInFileBrowser(_file));

		        //menu.add(openTerminal);
		        menu.add(showInExplorer);
		        menu.show(e.getComponent(), e.getX(), e.getY());
		    }
		});
	}
	
	private void openInFileBrowser(File file) {
	    try {
	        Desktop.getDesktop().open(file.getParentFile());
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}

}
