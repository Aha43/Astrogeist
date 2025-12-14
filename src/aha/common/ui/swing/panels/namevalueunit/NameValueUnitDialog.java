package aha.common.ui.swing.panels.namevalueunit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import aha.common.numbers.Unit;

public final class NameValueUnitDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JTextField nameField = new JTextField(20);
    private final JTextField valueField = new JTextField(20);
    private final JComboBox<Unit> unitCombo = new JComboBox<>(Unit.values());

    private NameValueUnitEntry result;

    private NameValueUnitDialog(Window parent, NameValueUnitEntry toEdit) {
        super(parent, "Entry", ModalityType.APPLICATION_MODAL);

        setTitle(toEdit == null ? "Add entry" : "Edit entry");

        if (toEdit != null) {
            nameField.setText(toEdit.getName());
            valueField.setText(Double.toString(toEdit.getValue().value()));
            unitCombo.setSelectedItem(toEdit.getUnit());
        }

        var formPanel = new JPanel(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Value:"), gbc);
        gbc.gridx = 1;
        formPanel.add(valueField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Unit:"), gbc);
        gbc.gridx = 1;
        formPanel.add(unitCombo, gbc);

        var okButton = new JButton("OK");
        var cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> onCancel());

        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        getRootPane().setDefaultButton(okButton);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void onOk() {
        var name = nameField.getText().trim();
        var value = valueField.getText().trim();
        var unit = (Unit) unitCombo.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Name is required.",
                "Validation error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Value is required.",
                "Validation error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        result = new NameValueUnitEntry(name, value, unit);
        dispose();
    }

    private void onCancel() { result = null; dispose(); }

    /**
     * <p>
     *   Shows the dialog and returns a new entry, or null if the user 
     *   cancelled. If {@code toEdit} is non-null, its values are used as 
     *   initial content.
     * </p>
     */
    public static NameValueUnitEntry showDialog(Window parent, 
    	NameValueUnitEntry toEdit) {
        
    	var dialog = new NameValueUnitDialog(parent, toEdit);
        dialog.setVisible(true); // blocks
        return dialog.result;
    }
}
