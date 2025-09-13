package astrogeist.engine.abstraction.jobs;

public interface JobProgressListener {
    /**
	 * <p>
	 *   Called once before work begins.
	 * </p>
	 * @param totalUnits
	 */
    void onStart(int totalUnits); // -1 if unknown
    
    /**
     * <p>
     *   Called when a unit succeeds.
     * </p>
     * @param unit
     * @param info
     */
    void onSuccess(Object unit, String info); // Object lets you pass Path, record, row etc.

    /**
     * <p>
     *   Called when a unit fails.
     * </p>
     * @param unit
     * @param error
     */
    void onFailure(Object unit, Throwable error);
    
    /**
     * <p>
     *   Optional free-form status messages.
     * </p>
     * @param message
     */
    void onMessage(String message);

    /**
     * <p>
     *   Called when the job finishes (even if failures occurred).
     * </p>
     */
    void onDone();
}
