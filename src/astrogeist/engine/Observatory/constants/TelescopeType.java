package astrogeist.engine.observatory.constants;

public enum TelescopeType {
    SOLAR_DEDICATED,   // H-alpha, CaK, white-light solar instruments
    GENERAL_PURPOSE,  // Visual + astro imaging, no strong specialization
    ASTRO_IMAGING,    // Imaging-first (RC, fast Newts, corrected systems)
    PLANETARY,        // High focal length, lunar/planetary emphasis
    CAMERA_LENS,      // DSLR / mirrorless photographic lenses
    OTHER
}
