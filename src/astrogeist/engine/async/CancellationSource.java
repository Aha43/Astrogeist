package astrogeist.engine.async;

public final class CancellationSource {
    private final CancellationToken token = new CancellationToken();
    public CancellationToken token() { return token; }
    public void cancel() { token.cancel(); }
}
