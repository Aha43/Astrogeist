package aha.common.net;

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

    /**
     * <p>
     *   Constructor.
     * </p>
     * @param statusCode   HTTP status code.
     * @param uri          Request URI.
     * @param responseBody Response body text.
     */
    public HttpRequestException(int statusCode, URI uri, String responseBody) {
        super("HTTP " + statusCode + " for " + uri +
              (responseBody != null && !responseBody.isBlank()
                  ? " body=" + snippet(responseBody)
                  : ""));
        this.statusCode = statusCode;
        this.uri = uri;
        this.responseBody = responseBody;
    }

    /**
     * <p>
     *   Gets the HTTP status code.
     * </p>
     * @return the HTTP status code.
     */
    public final int statusCode() { return statusCode; }
    
    /**
     * <p>
     *   Gets the URI.
     * </p>
     * @return the URI.
     */
    public final URI uri() { return uri; }
    
    /**
     * <p>
     *   Gets the response body.
     * </p>
     * @return
     */
    public final String responseBody() { return responseBody; }

    // Used to truncate the response body for exception message.
    private final static String snippet(String body) {
        return body.length() > 200 ? body.substring(0, 200) + "..." : body; }
}
