package aha.common.ui.swing.diagnostic.idnames;

import static java.util.Objects.requireNonNull;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import aha.common.abstraction.IdNames;

public final class ShowIdNamesDialogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final Frame frame;
	
	private final IdNames idNames;
	
	public ShowIdNamesDialogAction(IdNames idNames) { this(null, idNames); }
	
	public ShowIdNamesDialogAction(Frame frame, IdNames idNames) {
		super("Id label mapping");
		this.frame = frame;
		this.idNames = requireNonNull(idNames, "idNames");
	}

	@Override public final void actionPerformed(ActionEvent e) {
		IdNamesDialog.showDialog(this.frame, this.idNames); }

}
