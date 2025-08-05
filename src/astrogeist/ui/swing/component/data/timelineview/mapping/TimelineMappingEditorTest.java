package astrogeist.ui.swing.component.data.timelineview.mapping;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class TimelineMappingEditorTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Timeline Mapping Editor");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.getContentPane().add(new TimelineMappingEditor());
            f.setSize(800, 600);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

