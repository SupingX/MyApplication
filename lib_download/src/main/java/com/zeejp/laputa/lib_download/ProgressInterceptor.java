package com.zeejp.laputa.lib_download;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
public class ProgressInterceptor implements Interceptor {
    private DownloadListener listener;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), listener))
                .build();
    }

}
