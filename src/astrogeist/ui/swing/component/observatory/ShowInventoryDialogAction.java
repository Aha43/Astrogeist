package astrogeist.ui.swing.component.observatory;

import static astrogeist.ui.swing.component.observatory.InventoryTreePanel.showDialog;
import static java.util.Objects.requireNonNull;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import astrogeist.engine.observatory.Observatory;

public final class ShowInventoryDialogAction extends AbstractAction {
private static final long serialVersionUID = 1L;
	
	private final Frame parent;
	private final Observatory observatory;
	
	public ShowInventoryDialogAction(Frame parent, 
		Observatory observatory) {
		
		super("Inventory");
		this.parent = parent;
		this.observatory = requireNonNull(observatory, "observatory");
	}
	
	@Override public final void actionPerformed(ActionEvent e) {
		showDialog(parent, observatory); }
}
