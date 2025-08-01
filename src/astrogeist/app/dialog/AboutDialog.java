package astrogeist.app.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import astrogeist.app.App;
import astrogeist.resources.Resources;

public final class AboutDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    public AboutDialog(App app) {
        super(app.getFrame(), "About Astrogeist", true);
        setSize(400, 450);
        setLocationRelativeTo(app.getFrame());
        setLayout(new BorderLayout());

        // Top section with logo, title, and version
        var header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        URL logoUrl = Resources.getLogoUrl(this);
        if (logoUrl != null) {
            var icon = new ImageIcon(logoUrl);
            var scaledImage = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
            var logo = new JLabel(new ImageIcon(scaledImage));
            logo.setAlignmentX(Component.CENTER_ALIGNMENT);
            header.add(Box.createVerticalStrut(10));
            header.add(logo);
        }

        var title = new JLabel("Astrogeist", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        var version = new JLabel("Version 0.1", SwingConstants.CENTER);
        version.setFont(new Font("SansSerif", Font.PLAIN, 14));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(Box.createVerticalStrut(5));
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

        var scrollPane = new JScrollPane(text);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.setPreferredSize(new Dimension(380, 120));

        // Close button
        var close = new JButton("Close");
        close.addActionListener(e -> dispose());

        var buttonPanel = new JPanel();
        buttonPanel.add(close);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
