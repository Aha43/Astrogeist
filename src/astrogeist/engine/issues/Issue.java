package astrogeist.engine.issues;

import java.nio.file.Path;

public record Issue(Path path, String message, Exception exception, String solutionKey) {}
