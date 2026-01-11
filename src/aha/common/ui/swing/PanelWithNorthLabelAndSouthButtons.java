package aha.common.ui.swing;

import static java.util.Objects.requireNonNull;
import static aha.common.guard.LogicGuards.throwIfNot;
import static aha.common.guard.StringGuards.requireNonEmpty;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PanelWithNorthLabelAndSouthButtons  extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JComponent content = null;
	
	private final JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel south = null;
	
	public PanelWithNorthLabelAndSouthButtons() {
		super.setLayout(new BorderLayout());
		super.add(north, BorderLayout.NORTH);
	}
	
	public PanelWithNorthLabelAndSouthButtons(JComponent c) { 
		this(); content(c); }
	
	public final PanelWithNorthLabelAndSouthButtons content(JComponent c) {
		throwIfNot(content == null, "has content");
		this.content = requireNonNull(c, "c");
		super.add(c, BorderLayout.CENTER);
		return this;
	}
	
	public final PanelWithNorthLabelAndSouthButtons contentInScroll(
		JComponent c) {
		
		var scroll = new JScrollPane(requireNonNull(c, "c"));
		return content(scroll);
	}
	
	public final PanelWithNorthLabelAndSouthButtons label(String text) {
		this.north.add(new JLabel(requireNonEmpty(text, "text")));
		return this;
	}
	
	public final PanelWithNorthLabelAndSouthButtons button(String name, 
		ActionListener l) {
		
		var button = new JButton(requireNonEmpty(name, "name"));
		button.addActionListener(requireNonNull(l, "l"));
		if (south == null) {
			south = new JPanel(new FlowLayout(FlowLayout.CENTER));
			super.add(south, BorderLayout.SOUTH);
		}
		this.south.add(button);
		return this;
	}
	
	public final PanelWithNorthLabelAndSouthButtons button(Action action) {
		var button = new JButton(requireNonNull(action, "action"));
		this.south.add(button);
		return this;
	}
	
}
