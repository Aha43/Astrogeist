package aha.common.guard;

import static aha.common.guard.ObjectGuards.throwStaticClassInstantiateError;
import static aha.common.guard.StringGuards.requireNonEmpty;
import static aha.common.util.Strings.isNullOrBlank;
import static aha.common.util.Strings.quote;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

public final class PathGuards {
    private PathGuards() { throwStaticClassInstantiateError(); }

    /** Entry point for fluent file/path validation. */
    public static PathGuard require(Path path) { return new PathGuard(path); }

    /** Fluent path validator. */
    public static final class PathGuard {
        private final Path path;

        PathGuard(Path path) { this.path = requireNonNull(path, "path"); }

        public PathGuard exists() {
            if (!Files.exists(path)) throw ex("does not exist");
            return this;
        }

        public PathGuard isFile() {
            exists();
            if (!Files.isRegularFile(path)) throw ex("is not a regular file");
            return this;
        }

        public PathGuard isDirectory() {
            exists();
            if (!Files.isDirectory(path)) throw ex("is not a directory");
            return this;
        }

        /** File must exist, be a file, and have size > 0 bytes. */
        public PathGuard nonEmptyFile() {
            isFile();
            try {
                if (Files.size(path) <= 0) throw ex("is an empty file");
            } catch (IOException e) {
                throw ex("size could not be determined: " + e.getMessage(), e);
            }
            return this;
        }

        /** Directory must exist, be a dir, and contain at least one entry. */
        public PathGuard nonEmptyDirectory() {
            isDirectory();
            try (var stream = Files.list(path)) {
                if (!stream.findAny().isPresent()) 
                	throw ex("is an empty directory");
            } catch (IOException e) {
                throw ex("contents could not be listed: " + e.getMessage(), e);
            }
            return this;
        }

        /** Require exact extension (case-insensitive, dot optional). */
        public PathGuard hasExtension(String ext) {
            requireNonEmpty(ext, "ext");
            
            String expected = normalizeExt(ext);

            String filename = path.getFileName().toString();
            String fext = getExtension(filename);

            if (isNullOrBlank(fext)) throw ex("is missing an extension");

            String actual = normalizeExt(fext);
            if (!expected.equals(actual)) {
                throw ex("has extension " + quote(fext) +
                         " but expected " + quote(expected));
            }
            return this;
        }

        /** Require that path has one of many allowed extensions. */
        public PathGuard hasAnyExtension(String... exts) {
            requireNonNull(exts, "exts");
            if (exts.length == 0) 
                throw new IllegalArgumentException("exts must not be empty");

            String filename = path.getFileName().toString();
            String fext = getExtension(filename);

            if (isNullOrBlank(fext)) throw ex("is missing an extension");

            String actual = normalizeExt(fext);

            boolean match = Arrays.stream(exts)
            	.filter(Objects::nonNull)
            	.map(PathGuards::normalizeExt)
            	.anyMatch(actual::equals);

            if (!match)
                throw ex("has extension " + quote(fext) + 
                	" but expected one of " + Arrays.toString(exts));

            return this;
        }

        /** Returns the validated path. */
        public Path path() { return path; }

        // --- internal helpers ---

        private RuntimeException ex(String msg) {
            return new IllegalArgumentException("path " + quote(path) + " "
            	+ msg);
        }

        private RuntimeException ex(String msg, Throwable cause) {
            return new IllegalArgumentException("path " + quote(path) + " " +
            	msg, cause);
        }
    }

    private static String normalizeExt(String ext) {
        String s = ext.startsWith(".") ? ext.substring(1) : ext;
        return s.toLowerCase();
    }

    private static String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i == -1 || i == filename.length() - 1) return "";
        return filename.substring(i + 1);
    }
    
}
