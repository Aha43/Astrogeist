package astrogeist.engine.integration.api.astrometry.model;

public enum Visibility {
    PUBLIC("y"),
    PRIVATE("n");

    private final String code;
    
    Visibility(String code) { this.code = code; }
    
    public String code() { return code; }
}
