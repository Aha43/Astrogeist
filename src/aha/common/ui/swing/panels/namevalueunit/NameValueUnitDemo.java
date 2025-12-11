package aha.common.ui.swing.panels.namevalueunit;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import aha.common.numbers.Unit;

public final class NameValueUnitDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("Name/Value/Unit Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            var panel = new NameValueUnitPanel();
            panel.entries(List.of(
                new NameValueUnitEntry("Exposure time", "120", Unit.S),
                new NameValueUnitEntry("Gain", "150", Unit.MM)
                //new NameValueUnitEntry("Filter", "H-alpha", Unit.UnitLess)
            ));

            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

