package astrogeist.common.net;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class MultipartBody {
    private static final String CRLF = "\r\n";
    private final String boundary = "----astrogeist-" + UUID.randomUUID();
    private final List<byte[]> parts = new java.util.ArrayList<>();

    public final String contentType() { return "multipart/form-data; boundary=" + boundary; }

    public final MultipartBody addText(String name, String value) {
        var s = "--" + boundary + CRLF +
        	"Content-Disposition: form-data; name=\"" + name + "\"" + CRLF +
            "Content-Type: text/plain; charset=UTF-8" + CRLF + CRLF + value + CRLF;
        parts.add(s.getBytes(StandardCharsets.UTF_8));
        return this;
    }

    public final MultipartBody addFile(String name, Path file) throws IOException {
        var filename = file.getFileName().toString();
        var mime = Optional.ofNullable(Files.probeContentType(file))
        		.orElse("application/octet-stream");

        var header = "--" + boundary + CRLF +
        		"Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"" + CRLF +
                "Content-Type: " + mime + CRLF + CRLF;
        parts.add(header.getBytes(StandardCharsets.UTF_8));
        parts.add(Files.readAllBytes(file));
        parts.add(CRLF.getBytes(StandardCharsets.UTF_8));
        return this;
    }

    public final HttpRequest.BodyPublisher build() {
        byte[] end = ("--" + boundary + "--" + CRLF).getBytes(StandardCharsets.UTF_8);
        int total = end.length;
        for (byte[] p : parts) total += p.length;

        byte[] all = new byte[total];
        int off = 0;
        for (byte[] p : parts) { System.arraycopy(p, 0, all, off, p.length); off += p.length; }
        System.arraycopy(end, 0, all, off, end.length);

        // This publisher KNOWS the length; HttpClient will send Content-Length
        return HttpRequest.BodyPublishers.ofByteArray(all);
    }
    
}
