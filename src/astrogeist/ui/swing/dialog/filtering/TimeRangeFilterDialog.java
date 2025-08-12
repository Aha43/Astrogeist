package astrogeist.ui.swing.dialog.filtering;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import astrogeist.engine.timeline.view.TimeRangeTimelineViewFilter;

public final class TimeRangeFilterDialog extends JDialog {
    private static final long serialVersionUID = 1L;
	private final JTextField txtFrom = new JTextField(24);
    private final JTextField txtTo   = new JTextField(24);
    private boolean ok = false;
    private final ZoneId zone;

    private TimeRangeFilterDialog(Window owner) {
        super(owner, "Time range filter", ModalityType.APPLICATION_MODAL);
        
        this.zone = ZoneId.of("UTC");

        var hint = new JLabel("<html><i>Use ISO-8601 (e.g. 2025-08-10T21:30), leave empty for open range.</i></html>");

        var form = new JPanel(new GridBagLayout());
        var g = new GridBagConstraints();
        g.insets = new Insets(4,4,4,4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = 0; form.add(new JLabel("From (UTC/local ok)"), g);
        g.gridx = 1; form.add(txtFrom, g);
        g.gridx = 0; g.gridy = 1; form.add(new JLabel("To"), g);
        g.gridx = 1; form.add(txtTo, g);
        g.gridx = 0; g.gridy = 2; g.gridwidth = 2; form.add(hint, g);

        var btnOk = new JButton("OK");
        var btnCancel = new JButton("Cancel");
        btnOk.addActionListener(e -> { ok = validateInput(); if (ok) setVisible(false); });
        btnCancel.addActionListener(e -> setVisible(false));

        var buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(btnCancel); buttons.add(btnOk);

        getContentPane().setLayout(new BorderLayout(8,8));
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private boolean validateInput() {
        try {
            parseInstantOrNull(txtFrom.getText());
            parseInstantOrNull(txtTo.getText());
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid time", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private Instant parseInstantOrNull(String s) {
        s = s.trim();
        if (s.isEmpty()) return null;

        // Accept: Instant.parse() (with 'Z') OR local date-time (assume local zone)
        try { return Instant.parse(s); } catch (DateTimeParseException ignore) {}

        try {
            var ldt = LocalDateTime.parse(s);                 // e.g. 2025-08-10T21:30
            return ldt.atZone(zone).toInstant();
        } catch (DateTimeParseException ex) {
            // Accept plain date as midnight local
            try {
                var d = LocalDate.parse(s);                   // e.g. 2025-08-10
                return d.atStartOfDay(zone).toInstant();
            } catch (DateTimeParseException ex2) {
                throw new IllegalArgumentException("Could not parse time: “" + s + "”. Try 2025-08-10 or 2025-08-10T21:30 or 2025-08-10T21:30:00Z");
            }
        }
    }

    private Optional<TimeRangeTimelineViewFilter> result() {
        if (!ok) return Optional.empty();
        var from = parseInstantOrNull(txtFrom.getText());
        var to   = parseInstantOrNull(txtTo.getText());
        return Optional.of(new TimeRangeTimelineViewFilter(from, to));
    }

    // API
    public static Optional<TimeRangeTimelineViewFilter> show(Window owner) {
        var dlg = new TimeRangeFilterDialog(owner);
        dlg.setVisible(true);
        return dlg.result();
    }

    public static Optional<TimeRangeTimelineViewFilter> show(Window owner, TimeRangeTimelineViewFilter existing) {
        var dlg = new TimeRangeFilterDialog(owner);
        if (existing.from() != null) dlg.txtFrom.setText(existing.from().toString());
        if (existing.to()   != null) dlg.txtTo.setText(existing.to().toString());
        dlg.setVisible(true);
        return dlg.result();
    }
}

