package astrogeist.engine.runconfig.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RunConfig(
        @JsonProperty(value = "name", required = true) String name,
        @JsonProperty("description") String description,
        @JsonProperty(value = "kind", required = true) Kind kind,
        @JsonProperty(value = "runFolder", required = true) String runFolder,
        @JsonProperty("inputs") Map<String, String> inputs,
        @JsonProperty(value = "copy", required = true) List<CopyRule> copy,
        @JsonProperty(value = "command", required = true) Command command,
        @JsonProperty("postRun") PostRun postRun) {
	
    public enum Kind {
        SNAPSHOT, SESSION, GLOBAL;

        @JsonCreator
        public static Kind fromJson(String v) {
            if (v == null) return null;
            return switch (v.trim().toLowerCase()) {
                case "snapshot" -> SNAPSHOT;
                case "session"  -> SESSION;
                case "global"   -> GLOBAL;
                default -> throw new IllegalArgumentException(
                	"Unknown kind: " + v);
            };
        }
    }

    /** Lightweight validation; add more rules as your schema evolves. */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (isBlank(name))
        	errors.add("name is required");
        if (kind == null) 
        	errors.add("kind is required (snapshot|session|global)");
        if (isBlank(runFolder))
        	errors.add("runFolder is required");
        if (copy == null || copy.isEmpty())
        	errors.add("copy[] must contain at least one rule");
        if (command == null)
        	errors.add("command is required");
        else {
            if (isBlank(command.exec())) errors.add("command.exec is required");
            if (isBlank(command.cwd())) errors.add("command.cwd is required");
            if (command.timeoutSec() != null && command.timeoutSec() < 0)
                errors.add("command.timeoutSec must be >= 0");
        }
        if (copy != null) {
            for (int i = 0; i < copy.size(); i++) {
                var r = copy.get(i);
                if (isBlank(r.from()))
                	errors.add("copy[" + i + "].from is required");
                if (isBlank(r.to()))
                	errors.add("copy[" + i + "].to is required");
            }
        }
        return errors;
    }

    private static boolean isBlank(String s) {
    	return s == null || s.trim().isEmpty(); }
}
