package com.laputa.zeejp.lib_common.net;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 重定向
 */
public class RedictInterceptor implements Interceptor {
    private String newHost;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        //http://127.0.0.1/test/upload/img?userName=xiaoming&userPassword=12345
        String scheme = url.scheme();//  http https
        String host = url.host();//   127.0.0.1
        String path = url.encodedPath();//  /test/upload/img
        String query = url.encodedQuery();//  userName=xiaoming&userPassword=12345
        StringBuffer sb = new StringBuffer();
        String newUrl = sb.append(scheme).append(newHost).append(path).append("?").append(query).toString();
        Request.Builder builder = request.newBuilder().url(newUrl);
        return chain.proceed(builder.build());
    }
}
