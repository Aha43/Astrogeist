package aha.common.async;

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
     *   Tells if operation should be cancelled.
     * </p>
     * @return {@code true} if to cancel else {@code false}.
     */
    public final boolean isCancelled() { return cancelled.get(); }
    
    /**
     * <p>
     *   Invoked from
     *   {@link CancellationSource}.
     * </p>
     */
    final void cancel() { cancelled.set(true); }
}
