package astrogeist.engine.runconfig.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CopyRule(
		@JsonProperty(value = "from", required = true) String from,
        @JsonProperty("include") List<String> include,
        @JsonProperty("exclude") List<String> exclude,
        @JsonProperty(value = "to", required = true) String to,
        @JsonProperty("behavior") Behavior behavior) {
	
    public enum Behavior {
        OVERWRITE, SKIP, FAIL_ON_EXISTS;

        @JsonCreator
        public static Behavior fromJson(String v) {
            if (v == null) return null;
            return switch (v.trim().toLowerCase()) {
                case "overwrite"     -> OVERWRITE;
                case "skip"          -> SKIP;
                case "failonexists"  -> FAIL_ON_EXISTS;
                default -> throw new IllegalArgumentException(
                	"Unknown behavior: " + v);
            };
        }
    }
}
