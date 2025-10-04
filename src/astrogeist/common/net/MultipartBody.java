package astrogeist.common.net;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class MultipartBody {
    private static final String CRLF = "\r\n";
    private final String boundary = "----astrogeist-" + java.util.UUID.randomUUID();
    private final java.util.List<byte[]> parts = new java.util.ArrayList<>();

    public String contentType() { return "multipart/form-data; boundary=" + boundary; }

    public MultipartBody addText(String name, String value) {
        String s = "--" + boundary + CRLF +
                   "Content-Disposition: form-data; name=\"" + name + "\"" + CRLF +
                   "Content-Type: text/plain; charset=UTF-8" + CRLF + CRLF +
                   value + CRLF;
        parts.add(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return this;
    }

    public MultipartBody addFile(String name, java.nio.file.Path file) throws java.io.IOException {
        String filename = file.getFileName().toString();
        String mime = java.util.Optional.ofNullable(java.nio.file.Files.probeContentType(file))
                        .orElse("application/octet-stream");

        String header = "--" + boundary + CRLF +
                        "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"" + CRLF +
                        "Content-Type: " + mime + CRLF + CRLF;
        parts.add(header.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        parts.add(java.nio.file.Files.readAllBytes(file));
        parts.add(CRLF.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return this;
    }

    public java.net.http.HttpRequest.BodyPublisher build() {
        byte[] end = ("--" + boundary + "--" + CRLF).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        int total = end.length;
        for (byte[] p : parts) total += p.length;

        byte[] all = new byte[total];
        int off = 0;
        for (byte[] p : parts) { System.arraycopy(p, 0, all, off, p.length); off += p.length; }
        System.arraycopy(end, 0, all, off, end.length);

        // This publisher KNOWS the length; HttpClient will send Content-Length
        return java.net.http.HttpRequest.BodyPublishers.ofByteArray(all);
    }



//    private static byte[] concat(byte[]... arrays) {
//        int len = 0; for (byte[] a : arrays) len += a.length;
//        byte[] out = new byte[len];
//        int p = 0; for (byte[] a : arrays) { System.arraycopy(a, 0, out, p, a.length); p += a.length; }
//        return out;
//    }
    
}
