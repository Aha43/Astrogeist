package astrogeist.ui.swing.component.observatory;

import static java.util.Objects.requireNonNull;
import static javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public final class InstrumentPickerPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public interface SelectionListener {
		void selectionChanged(List<String> selectedInstrumentNames); }

	private final JTextField filterField = new JTextField();
	private final DefaultListModel<String> listModel = new DefaultListModel<>();
	private final JList<String> instrumentList = new JList<>(listModel);

	private final JLabel selectedCountLabel = new JLabel("Selected: 0");
	private final JButton clearButton = new JButton("Clear");

	private final List<String> allInstrumentNamesOrdered = new ArrayList<>();
	private final Set<String> selectedNames = new LinkedHashSet<>();

	private final List<SelectionListener> listeners = new ArrayList<>();

	private boolean programmatic = false;

	public InstrumentPickerPanel() {
		super(new BorderLayout(6, 6));
		setBorder(BorderFactory.createTitledBorder("Instruments"));

		var top = new JPanel(new BorderLayout(6, 6));
		top.add(new JLabel("Filter:"), BorderLayout.WEST);
		top.add(filterField, BorderLayout.CENTER);
		top.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

		instrumentList.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
		var scroll = new JScrollPane(instrumentList);
		scroll.setPreferredSize(new Dimension(260, 400));

		var bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
		bottom.add(selectedCountLabel);
		bottom.add(clearButton);

		add(top, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);

		wire();
	}

	public final void setInstrumentNames(List<String> instrumentNamesOrdered) {
		requireNonNull(instrumentNamesOrdered, "instrumentNamesOrdered");
		allInstrumentNamesOrdered.clear();
		allInstrumentNamesOrdered.addAll(instrumentNamesOrdered);
		refreshModel();
		purgeMissingSelections();
		applySelection();
		fire();
	}

	public final List<String> getSelectedInstrumentNames() {
		return List.copyOf(selectedNames); }

	public final void addSelectionListener(SelectionListener l) {
		listeners.add(requireNonNull(l)); }

	// ---- internals ----

	private void wire() {
		filterField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { refreshModel(); }
			public void removeUpdate(DocumentEvent e) { refreshModel(); }
			public void changedUpdate(DocumentEvent e) { refreshModel(); }
		});

		instrumentList.addListSelectionListener(e -> {
			if (e.getValueIsAdjusting() || programmatic) return;
			selectedNames.clear();
			for (var s : instrumentList.getSelectedValuesList()) {
				selectedNames.add(s); }
			updateCount();
			fire();
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
		for (var n : allInstrumentNamesOrdered)
			if (q.isEmpty() || n.toLowerCase(Locale.ROOT).contains(q))
				listModel.addElement(n);
		applySelection();
	}

	private void applySelection() {
		programmatic = true;
		try {
			instrumentList.clearSelection();
			List<Integer> idx = new ArrayList<>();
			for (int i = 0; i < listModel.size(); i++)
				if (selectedNames.contains(listModel.get(i))) idx.add(i);
			instrumentList.setSelectedIndices(idx.stream().mapToInt(
				Integer::intValue).toArray());
			updateCount();
		} finally {
			programmatic = false;
		}
	}

	private void purgeMissingSelections() {
		selectedNames.removeIf(n -> !allInstrumentNamesOrdered.contains(n)); }

	private void updateCount() {
		selectedCountLabel.setText("Selected: " + selectedNames.size()); }

	private void fire() {
		var snapshot = List.copyOf(selectedNames);
		invokeLater(() -> listeners.forEach(l -> l.selectionChanged(snapshot)));
	}
}
