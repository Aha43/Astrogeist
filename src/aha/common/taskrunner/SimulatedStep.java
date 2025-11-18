package aha.common.taskrunner;

import aha.common.abstraction.taskrunner.TaskStep;

import java.util.Random;

/**
 * <p>
 *   {@link TaskStep} for testing and demo.
 * </p>
 */
public final class SimulatedStep implements TaskStep {

    private static final Random RANDOM = new Random();

    private final String id;
    private final String label;
    private final int weight;
    private final int ticks;
    private final boolean mayFail;

    /**
     * <p>
     *    Constructor.
     * </p>
     * @param id       stable id for logging
     * @param label    human-readable label
     * @param weight   relative progress weight
     * @param ticks    how many increments (higher = longer)
     * @param mayFail  if true, sometimes throws to simulate errors
     */
    public SimulatedStep(String id, String label, int weight, int ticks,
    	boolean mayFail) {
        
    	this.id = id;
        this.label = label;
        this.weight = weight;
        this.ticks = ticks;
        this.mayFail = mayFail;
    }

    @Override public final String id() { return id; }
    @Override public final String label() { return label; }
    @Override public final int weight() { return weight; }

    @Override public final void run(TaskRunContext ctx) throws Exception {
        ctx.log(label + " started");

        for (int i = 1; i <= ticks; i++) {
            ctx.checkCancelled(); // cooperates with Cancel button

            // Simulate some "work"
            try {
                Thread.sleep(120L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new TaskRunCancelledException();
            }

            int stepPercent = (i * 100) / ticks;
            ctx.progress(id, stepPercent, label + " (" + stepPercent + "%)");

            // Optional: simulate random failure
            if (mayFail && i > ticks / 3 && RANDOM.nextDouble() < 0.03) {
                throw new RuntimeException("Simulated failure in " + label);
            }
        }

        ctx.log(label + " completed");
    }
    
}
