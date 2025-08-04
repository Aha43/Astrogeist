package astrogeist.app.dialog.selection;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import astrogeist.app.App;
import astrogeist.app.component.selection.SelectionTablePanel;

public final class SelectionDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final SelectionTablePanel selectionPanel;

    private SelectionDialog(App app, String title, List<String> selectedItems, Set<String> allItems) {
    	super(app.getFrame(), title, true);
    	
    	setLayout(new BorderLayout());

        selectionPanel = new SelectionTablePanel(selectedItems, allItems);
        add(selectionPanel, BorderLayout.CENTER);

        var buttonPanel = new JPanel();
        var ok = new JButton("OK");
        var cancel = new JButton("Cancel");
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        add(buttonPanel, BorderLayout.SOUTH);

        ok.addActionListener(e -> {
            selectedItems.clear();
            selectedItems.addAll(selectionPanel.getSelectedItems());
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        pack();
        setSize(400, 400);
        setLocationRelativeTo(app.getFrame());
    }

    public static void show(App app, String title, List<String> selectedItems, Set<String> allItems) {
        new SelectionDialog(app, title, selectedItems, allItems).setVisible(true); }
}

