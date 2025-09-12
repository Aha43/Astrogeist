package astrogeist.engine.async;

import java.util.concurrent.atomic.AtomicBoolean;

public final class CancellationToken {
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    public boolean isCancelled() { return cancelled.get(); }
    void cancel() { cancelled.set(true); }
}
