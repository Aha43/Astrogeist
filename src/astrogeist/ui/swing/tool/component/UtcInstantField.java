package astrogeist.ui.swing.tool.component;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

public final class UtcInstantField extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JSpinner spinner;

    public UtcInstantField(Instant initialUtc) {
        setLayout(new BorderLayout(6, 0));

        spinner = new JSpinner(new SpinnerDateModel(
            Date.from(initialUtc), null, null, java.util.Calendar.MINUTE));

        var editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm:ss 'UTC'");
        SimpleDateFormat fmt = editor.getFormat();
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        spinner.setEditor(editor);

        add(new JLabel("Observation (UTC):"), BorderLayout.WEST);
        add(spinner, BorderLayout.CENTER);
    }

    public Instant getInstant() {
        return ((Date) spinner.getValue()).toInstant();
    }

    public void setInstant(Instant utc) {
        spinner.setValue(Date.from(utc));
    }
}

