package astrogeist.ui.swing.scanning;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public final class RefreshAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final ScannersSelectionPanel scannersSelectionPanel;
	
	public RefreshAction(ScannersSelectionPanel scannersSelectionPanel) {
		super("Refresh");
		this.scannersSelectionPanel = scannersSelectionPanel;
	}

	@Override public final void actionPerformed(ActionEvent e) {
		this.scannersSelectionPanel.refreshCanScan(); }

}
