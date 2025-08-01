package astrogeist.scanner.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScanConfig {
    private final List<TimestampConfig> timestampResolvers = new ArrayList<>();
    private final List<SubjectConfig> subjectResolvers = new ArrayList<>();
    private final List<SoftwareConfig> softwareResolvers = new ArrayList<>();

    public List<TimestampConfig> getTimestampResolvers() { return timestampResolvers; }
    public List<SubjectConfig> getSubjectResolvers() { return subjectResolvers; }
    public List<SoftwareConfig> getSoftwareResolvers() { return softwareResolvers; }
    public void addTimestampResolver(TimestampConfig config) { timestampResolvers.add(config); }
    public void addSubjectResolver(SubjectConfig config) { subjectResolvers.add(config); }

    public void addSoftwareResolver(SoftwareConfig config) { softwareResolvers.add(config); }

    // --- Inner Config Types ---

    public static class TimestampConfig {
        private final String regex;
        private final String format;
        private final String timezone;

        public TimestampConfig(String regex, String format, String timezone) {
            this.regex = regex;
            this.format = format;
            this.timezone = timezone;
        }

        public String getRegex() { return regex; }
        public String getFormat() { return format; }
        public String getTimezone() { return timezone; }
    }

    public static class SubjectConfig {
        private final String regex;

        public SubjectConfig(String regex) { this.regex = regex; }
        public String getRegex() { return regex; }
    }

    public static class SoftwareConfig {
        private final String regex;
        private final Map<String, String> mapping;

        public SoftwareConfig(String regex, Map<String, String> mapping) {
            this.regex = regex;
            this.mapping = mapping;
        }

        public String getRegex() { return regex; }
        public Map<String, String> getMapping() { return mapping; }
    }
}
