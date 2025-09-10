package astrogeist.ui.swing.progress;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public final class JobProgressDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
	private final JobsProgressPanel panel = new JobsProgressPanel();
    private final JButton closeBtn = new JButton("Close");

    public JobProgressDialog(Window owner, String title) {
        super(owner, title, ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        closeBtn.addActionListener(e -> dispose());

        var btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(closeBtn);

        getContentPane().setLayout(new BorderLayout(8,8));
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);

        setSize(900, 500);
        setLocationRelativeTo(owner);
    }

    public JobsProgressPanel getPanel() { return panel; }

    public void setJobs(List<JobProgress> jobs) { panel.setJobs(jobs); }
}
