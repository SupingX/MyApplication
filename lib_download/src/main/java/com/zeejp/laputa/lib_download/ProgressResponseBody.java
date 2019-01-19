package com.zeejp.laputa.lib_download;



import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class ProgressResponseBody extends ResponseBody {
    private DownloadListener listener;
    private ResponseBody responseBody;
    private BufferedSource bufferedSource;


    public ProgressResponseBody(ResponseBody responseBody,DownloadListener listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        return performResource();
    }

    private BufferedSource performResource() {

        return null;
    }


}
