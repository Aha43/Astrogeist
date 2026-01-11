package aha.common.ui.swing;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

public final class PopupMenus {
    private PopupMenus() { throwStaticClassInstantiateError(); }

    /**
     * <p>
     *   Installs a popup menu trigger handler on the component.
     *   The menu is provided by supplier so it may be created lazily or
     *   dynamically.
     * </p>
     */
    public static void install(JComponent component, PopupSupplier supplier) {
        component.addMouseListener(new MouseAdapter() {
            
        	@Override public void mousePressed(MouseEvent e)  { maybeShow(e); }
        	@Override public void mouseReleased(MouseEvent e) { maybeShow(e); }

        	private void maybeShow(MouseEvent e) {
        		if (!e.isPopupTrigger()) return;

        		JPopupMenu menu = supplier.getPopup(e);
        		if (menu == null) return;

        		menu.show(e.getComponent(), e.getX(), e.getY());
        	}
        });
    }
    
    public static void install(JComponent component, JPopupMenu menu) {
    	
    }

    @FunctionalInterface
    public interface PopupSupplier {
        /**
         * Build/return the popup menu for this event (or return null to 
         * show nothing).
         */
        JPopupMenu getPopup(MouseEvent e);
    }
}
