package astrogeist.engine.runconfig.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PostRun(
        @JsonProperty("openPaths") List<String> openPaths,
        @JsonProperty("showSummary") Boolean showSummary) {}
