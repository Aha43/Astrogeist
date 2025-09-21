package astrogeist.engine.async;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 *   Propagates notification that operations should be canceled.
 * </p>
 */
public final class CancellationToken {
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    
    /**
     * <p>
     *   Tells if communicate operation should be cancelled.
     * </p>
     * @return {@code true} if to cancel else {@code false}.
     */
    public boolean isCancelled() { return cancelled.get(); }
    
    void cancel() { cancelled.set(true); }
}
