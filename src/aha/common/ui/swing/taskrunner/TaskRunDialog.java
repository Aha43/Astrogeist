package aha.common.ui.swing.taskrunner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import aha.common.abstraction.taskrunner.TaskStep;
import aha.common.taskrunner.TaskEvent;
import aha.common.taskrunner.TaskRunner;
import aha.common.util.AttributeObject;

public final class TaskRunDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
	private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JTextArea logArea = new JTextArea(12, 60);
    private final JLabel statusLabel = new JLabel("Ready");
    private final JButton cancelButton = new JButton("Cancel");
    
    public TaskRunDialog(Window owner, String title, 
    	List<? extends TaskStep> steps) {
    	
    	this(owner, title, steps, new AttributeObject());
    }

    public TaskRunDialog(
    	Window owner,
    	String title, 
    	List<? extends TaskStep> steps,
    	AttributeObject contextData) {
    	
        super(owner, title, ModalityType.APPLICATION_MODAL);
        
        logArea.setEditable(false);

        setLayout(new BorderLayout());

        var top = new JPanel(new BorderLayout());
        top.add(statusLabel, BorderLayout.NORTH);
        top.add(progressBar, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(cancelButton);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);

        BlockingQueue<TaskEvent> events = new LinkedBlockingQueue<>();
        
        var runner = new TaskRunner(steps, events::offer);

        cancelButton.addActionListener(e -> {
            events.offer(TaskEvent.state(TaskEvent.StateType.CANCELLED, 
            	"User requested cancel"));
            runner.context().cancel();
            cancelButton.setEnabled(false);
        });

        // Background worker
        SwingWorker<Void, TaskEvent> worker = new SwingWorker<>() {
            @Override protected Void doInBackground() {
                runner.run(contextData);
                return null;
            }

            // not used; we poll manually below
            @Override protected void process(List<TaskEvent> chunks) { }
            @Override protected void done() { cancelButton.setEnabled(false); }
        };

        // Event pump: on EDT, update UI from queue
        Timer timer = new Timer(80, e -> {
            TaskEvent ev;
            while ((ev = events.poll()) != null) handleEvent(ev);
        });

        timer.start();
        worker.execute();
    }

    private void handleEvent(TaskEvent ev) {
        switch (ev) {
            case TaskEvent.Log log -> {
                logArea.append(log.message() + "\n");
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }
            case TaskEvent.Error err -> {
                logArea.append("ERROR: " + err.message() + "\n");
                statusLabel.setText("ERROR");
                progressBar.setForeground(Color.RED);
            }
            case TaskEvent.Progress p -> {
                progressBar.setValue(p.overallPercent());
                if (p.message() != null && !p.message().isEmpty()) {
                    statusLabel.setText(p.message());
                }
            }
            case TaskEvent.State st -> {
                switch (st.type()) {
                    case STARTED -> statusLabel.setText("Starting…"); 
                    case STEP_STARTED -> statusLabel.setText("Running: " +
                    	st.detail());
                    case STEP_FAILED -> statusLabel.setText("Failed in: " +
                    	st.detail());
                    case STEP_SUCCEEDED -> statusLabel.setText(
                    	"Step succeeded: " + st.detail());
                    case SUCCEEDED -> {
                        statusLabel.setText("Completed");
                        progressBar.setValue(100);
                    }
                    case FAILED -> statusLabel.setText("Failed: " +
                    	st.detail());
                    case CANCELLED -> statusLabel.setText("Cancelled");
                    default -> throw new IllegalArgumentException(
                    	"Unexpected value: " + st.type());
                }
            }
        }
    }
    
}
