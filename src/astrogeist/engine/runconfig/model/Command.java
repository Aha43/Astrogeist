package astrogeist.engine.runconfig.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Command(
        @JsonProperty(value = "exec", required = true) String exec,
        @JsonProperty("args") List<String> args,
        @JsonProperty(value = "cwd", required = true) String cwd,
        @JsonProperty("timeoutSec") Integer timeoutSec,
        @JsonProperty("dryRun") DryRunMode dryRun) {
	
    public enum DryRunMode {
        SKIP, DESCRIBE, RUN;

        @JsonCreator
        public static DryRunMode fromJson(String v) {
            if (v == null) return null;
            return switch (v.trim().toLowerCase()) {
                case "skip"      -> SKIP;
                case "describe"  -> DESCRIBE;
                case "run"       -> RUN;
                default -> throw new IllegalArgumentException(
                	"Unknown dryRun: " + v);
            };
        }
    }
}
