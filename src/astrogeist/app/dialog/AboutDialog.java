package astrogeist.app.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public final class AboutDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public AboutDialog(Frame owner) {
        super(owner, "About Astrogeist", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Top section with title and version
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Astrogeist", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version 0.1", SwingConstants.CENTER);
        version.setFont(new Font("SansSerif", Font.PLAIN, 14));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(version);
        header.add(Box.createVerticalStrut(10));

        // License / info text
        JTextArea text = new JTextArea("""
            Astrogeist is a tool for organizing and analyzing
            astronomical observation data.

            Created by Arne Halvorsen, 2025.

            Licensed under the MIT License.
        """);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(getBackground());

        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.setPreferredSize(new Dimension(380, 120));

        // Close button
        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(close);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

