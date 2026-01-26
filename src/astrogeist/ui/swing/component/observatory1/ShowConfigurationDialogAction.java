package astrogeist.ui.swing.component.observatory1;

import static astrogeist.ui.swing.component.observatory1.ConfigurationsPanel.showDialog;
import static java.util.Objects.requireNonNull;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import astrogeist.engine.observatory1.Configuration;
import astrogeist.engine.observatory1.Observatory;

/**
 * <p>
 *   {@link Action} to show
 *   {@link Configuration}s in a dialog provided by invoking
 *   {@link ConfigurationsPanel#showDialog(Observatory)}.
 * </p>
 */
public final class ShowConfigurationDialogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Frame parent;
	private final Observatory observatory;
	
	/**
	 * <p>
	 *   Constructor.
	 * </p>
	 * @param observatory {@link Observatory} to show 
	 *                    {@link Configuration}s off.
	 * @throws NullPointerException if {@code observatory} is {@code null}.
	 */
	public ShowConfigurationDialogAction(Frame parent, 
		Observatory observatory) {
		
		super("Configurations");
		this.parent = parent;
		this.observatory = requireNonNull(observatory, "observatory");
	}

	@Override public final void actionPerformed(ActionEvent e) {
		showDialog(this.parent, this.observatory, null); }
}
