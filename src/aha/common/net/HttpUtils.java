package aha.common.net;

import java.net.http.HttpResponse;

import aha.common.Guards;

public final class HttpUtils {
    private HttpUtils() { Guards.throwStaticClassInstantiateError(); }

    public final static <T> HttpResponse<T> ensureSuccess(
    	HttpResponse<T> resp) {
        
    	int code = resp.statusCode();
        if (code < 200 || code >= 300) {
            throw new HttpRequestException(code, resp.request().uri(),
            	String.valueOf(resp.body()));
        }
        return resp;
    }
    
}
