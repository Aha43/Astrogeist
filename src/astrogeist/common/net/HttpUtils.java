package astrogeist.common.net;

import java.net.http.HttpResponse;

import astrogeist.common.Common;

public final class HttpUtils {
    private HttpUtils() { Common.throwStaticClassInstantiateError(); }

    public final static <T> HttpResponse<T> ensureSuccess(HttpResponse<T> resp) {
        int code = resp.statusCode();
        if (code < 200 || code >= 300) {
            throw new HttpRequestException(code, resp.request().uri(),
            	String.valueOf(resp.body()));
        }
        return resp;
    }
    
}
