package astrogeist.common.net;

import java.net.URI;

/**
 * <p>
 *   Generic exception for non-successful HTTP responses.
 *   Similar to C#'s HttpRequestException / EnsureSuccessStatusCode().
 * </p>
 */
public final class HttpRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
	private final int statusCode;
    private final URI uri;
    private final String responseBody;

    public HttpRequestException(int statusCode, URI uri, String responseBody) {
        super("HTTP " + statusCode + " for " + uri +
              (responseBody != null && !responseBody.isBlank()
                  ? " body=" + snippet(responseBody)
                  : ""));
        this.statusCode = statusCode;
        this.uri = uri;
        this.responseBody = responseBody;
    }

    public final int statusCode() { return statusCode; }
    public final URI uri() { return uri; }
    public final String responseBody() { return responseBody; }

    private static String snippet(String body) {
        return body.length() > 200 ? body.substring(0, 200) + "..." : body; }
}

