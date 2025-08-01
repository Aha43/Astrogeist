package astrogeist.scanner.regex;

public class SubjectRegexResolver extends RegexExtractor<String> {
    public SubjectRegexResolver(String regex) {
        super(regex, matcher -> matcher.group(1)); // Extract subject
    }
}
