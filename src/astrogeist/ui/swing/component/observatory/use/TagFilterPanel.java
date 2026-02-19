package astrogeist.ui.swing.component.observatory.use;

import static java.util.Objects.requireNonNull;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import astrogeist.ui.swing.component.observatory.events.TagSelectionListener;

public final class TagFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Set<String> selected = new HashSet<>();
	private final List<TagSelectionListener> listeners = new ArrayList<>();

	public TagFilterPanel() {
		super(new FlowLayout(FlowLayout.LEFT, 6, 6));
		setBorder(BorderFactory.createTitledBorder("Tags"));
	}

	public final void setAvailableTags(Set<String> tags) {
		removeAll();
		selected.clear();

		for (var tag : tags) {
			var b = new JToggleButton(tag);
			b.addActionListener(e -> {
				if (b.isSelected()) selected.add(tag);
				else selected.remove(tag);
				fire();
			});
			add(b);
		}

		revalidate();
		repaint();
		fire();
	}

	public final Set<String> selectedTags() { return Set.copyOf(selected); }

	public final void addSelectionListener(TagSelectionListener l) {
		listeners.add(requireNonNull(l)); }
	
	public final void removeSelectionListener(TagSelectionListener l) {
		listeners.remove(requireNonNull(l)); }

	private void fire() {
		var snapshot = Set.copyOf(selected);
		for (var l : listeners) l.selectionChanged(snapshot);
	}
	
}
