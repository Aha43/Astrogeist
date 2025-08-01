package astrogeist.scanner.regex;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexExtractor<T> {
    protected final Pattern pattern;
    protected final Function<Matcher, T> extractor;

    public RegexExtractor(String regex, Function<Matcher, T> extractor) {
        this.pattern = Pattern.compile(regex);
        this.extractor = extractor;
    }

    public Optional<T> extract(Path path) {
        Matcher matcher = pattern.matcher(path.toString().replace(File.separatorChar, '/'));
        if (matcher.find()) {
            try {
                return Optional.ofNullable(extractor.apply(matcher));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}

