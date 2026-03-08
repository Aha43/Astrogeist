package aha.common.ui.swing.diagnostic.idnames;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import aha.common.abstraction.IdNames;

public final class IdNamesDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private IdNamesDialog(Frame parent, IdNames idNames) {
		super(parent, "Id label mapping");
		var table = new JTable(new IdNamesTableModel(idNames));
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		super.add(new JScrollPane(table), BorderLayout.CENTER);
		var close = new JButton("Close");
		close.addActionListener(e -> dispose());
		super.add(close, BorderLayout.SOUTH);
		pack();
	}
	
	public static final void showDialog(Frame parent, IdNames idNames) {
		requireNonNull(idNames, "idNames");
		new IdNamesDialog(parent, idNames).setVisible(true);
	}
}
