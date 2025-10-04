package astrogeist.engine.integration.api.astrometry.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class UploadOptions extends LinkedHashMap<String, Object> implements Map<String, Object> {
    private static final long serialVersionUID = 1L;

	public final UploadOptions visibility(Visibility v) {
        put("publicly_visible", v.code());
        return this;
    }

    public final UploadOptions allowCommercialUse(TriState v) {
        put("allow_commercial_use", v.code());
        return this;
    }

    public final UploadOptions allowModifications(TriState v) {
        put("allow_modifications", v.code());
        return this;
    }

    public final static UploadOptions defaults() {
        return new UploadOptions()
            .visibility(Visibility.PUBLIC)
            .allowCommercialUse(TriState.DEFAULT)
            .allowModifications(TriState.DEFAULT);
    }

    public final Map<String, Object> asMap() { return this; }
}
