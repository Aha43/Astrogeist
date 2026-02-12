package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import astrogeist.ui.swing.component.observatory.events.ItemSelectionListener;

public final class ItemPickerPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JTextField filterField = new JTextField();
	private final DefaultListModel<String> listModel = new DefaultListModel<>();
	private final JList<String> itemList = new JList<>(listModel);

	private final JLabel selectedCountLabel = new JLabel("Selected: 0");
	private final JButton clearButton = new JButton("Clear");

	private final List<String> allItemNamesOrdered = new ArrayList<>();
	private final Set<String> selectedNames = new LinkedHashSet<>();

	private final List<ItemSelectionListener> listeners = new ArrayList<>();

	public ItemPickerPanel() {
		super(new BorderLayout(6, 6));
		setBorder(BorderFactory.createTitledBorder("Items"));

		var top = new JPanel(new BorderLayout(6, 6));
		top.add(new JLabel("Filter:"), BorderLayout.WEST);
		top.add(filterField, BorderLayout.CENTER);
		top.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

		itemList.setSelectionMode(SINGLE_SELECTION);
		var scroll = new JScrollPane(itemList);
		scroll.setPreferredSize(new Dimension(260, 400));

		itemList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
			var cb = new JCheckBox(value);
			cb.setSelected(selectedNames.contains(value));
			cb.setOpaque(true);
			cb.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
			cb.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
			return cb;
		});

		itemList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int idx = itemList.locationToIndex(e.getPoint());
				if (idx < 0)
					return;

				// Ensure the row becomes the list's selected row (focus)
				itemList.setSelectedIndex(idx);

				var name = listModel.get(idx);
				if (selectedNames.contains(name))
					selectedNames.remove(name);
				else
					selectedNames.add(name);

				itemList.repaint(itemList.getCellBounds(idx, idx));
				updateCount();
				fire();

				// Prevent default selection behavior from interfering
				e.consume();
			}
		});

		itemList.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "toggle");
		itemList.getActionMap().put("toggle", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(java.awt.event.ActionEvent e) {
				int idx = itemList.getSelectedIndex();
				if (idx < 0)
					return;
				String name = listModel.get(idx);
				if (selectedNames.contains(name))
					selectedNames.remove(name);
				else
					selectedNames.add(name);
				itemList.repaint(itemList.getCellBounds(idx, idx));
				updateCount();
				fire();
			}
		});

		var bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
		bottom.add(selectedCountLabel);
		bottom.add(clearButton);

		add(top, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);

		wire();
	}

	public final void setItemNames(List<String> itemNamesOrdered) {
		requireNonNull(itemNamesOrdered, "itemNamesOrdered");
		allItemNamesOrdered.clear();
		allItemNamesOrdered.addAll(itemNamesOrdered);
		refreshModel();
		purgeMissingSelections();
		applySelection();
		fire();
	}

	public final List<String> getSelectedItemNames() {
		return List.copyOf(selectedNames);
	}

	public final void addSelectionListener(ItemSelectionListener l) {
		listeners.add(requireNonNull(l));
	}

	public final void removeSelectionListener(ItemSelectionListener l) {
		listeners.remove(requireNonNull(l));
	}

	/**
	 * <p>
	 * Replaces the current selection with the given item names.
	 * </p>
	 * <p>
	 * Names not present in the current item universe are ignored.
	 * </p>
	 * 
	 * @param itemNames names to select (order is preserved in the internal set)
	 * @throws NullPointerException if {@code itemNames} is null
	 */
	public final void setSelectedItemNames(List<String> itemNames) {
		requireNonNull(itemNames, "itemNames");

		selectedNames.clear();

		// Keep the selection order stable and consistent with the list order:
		// iterate over allItemNamesOrdered, selecting those requested.
		var requested = new HashSet<>(itemNames);
		for (var n : allItemNamesOrdered)
			if (requested.contains(n))
				selectedNames.add(n);

		// Optionally: clear filter so user sees selected items
		filterField.setText("");

		applySelection();
		fire();
	}

	// ---- internals ----

	private void wire() {
		filterField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				refreshModel();
			}

			public void removeUpdate(DocumentEvent e) {
				refreshModel();
			}

			public void changedUpdate(DocumentEvent e) {
				refreshModel();
			}
		});

		clearButton.addActionListener(e -> {
			selectedNames.clear();
			applySelection();
			fire();
		});
	}

	private void refreshModel() {
		var q = filterField.getText().trim().toLowerCase(Locale.ROOT);
		listModel.clear();
		for (var n : allItemNamesOrdered)
			if (q.isEmpty() || n.toLowerCase(Locale.ROOT).contains(q))
				listModel.addElement(n);
		applySelection();
	}

	private void applySelection() {
		// keep selection stable if you like;
		// but checkbox state is renderer-driven
		itemList.repaint();
		updateCount();
	}

	private void purgeMissingSelections() {
		selectedNames.removeIf(n -> !allItemNamesOrdered.contains(n));
	}

	private void updateCount() {
		selectedCountLabel.setText("Selected: " + selectedNames.size());
	}

	private void fire() {
		var snapshot = List.copyOf(selectedNames);
		for (var l : listeners)
			l.selectionChanged(snapshot);
	}

}
