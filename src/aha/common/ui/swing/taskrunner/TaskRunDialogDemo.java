package aha.common.ui.swing.taskrunner;


import javax.swing.*;

import aha.common.logging.Log;
import aha.common.taskrunner.SimulatedStep;

import java.util.List;
import java.util.logging.Level;

public final class TaskRunDialogDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	
        	Log.setGlobalLevel(Level.ALL);
        	
        	var logger = Log.get(TaskRunDialog.class);
        	logger.info("DEMO OF RUNNER");
        	
        	JFrame host = new JFrame("Task Runner Demo Host");
            host.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            host.setSize(400, 200);
            host.setLocationRelativeTo(null);
            host.setVisible(true);

            var steps = List.of(
            	new SimulatedStep("warmup", "Warming up", 10, 15, false),
            	new SimulatedStep("copy", "Copying FITS/ SER files", 60, 40, 
            		false),
            	new SimulatedStep("siril", "Running fake Siril", 30, 25, 
            		false)
            );

            TaskRunDialog dialog = new TaskRunDialog(
            	host, 
            	"Simulated run configuration", 
            	steps);
            
            dialog.setVisible(true);
        });
    }
}

