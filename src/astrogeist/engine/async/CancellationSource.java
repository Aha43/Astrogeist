package astrogeist.engine.async;

/**
 * <p>
 *   Signals to a CancellationToken that it should be canceled.
 * </p>
 */
public final class CancellationSource {
    private final CancellationToken token = new CancellationToken();
    
    /**
     * <p>
     *   The 
     *   {@link CancellationToken token}.
     * </p>
     * @return {@code token}
     */
    public CancellationToken token() { return token; }
    
    /**
     * <p>
     *   Tells the token it should be cancelled.
     * </p>
     */
    public void cancel() { token.cancel(); }
}
