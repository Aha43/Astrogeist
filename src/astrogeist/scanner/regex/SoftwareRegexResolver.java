package astrogeist.scanner.regex;

public class SoftwareRegexResolver extends RegexExtractor<String> {
    public SoftwareRegexResolver(String regex) {
        super(regex, matcher -> matcher.group(1)); // Extract software/tool name
    }
}
