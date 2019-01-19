package com.zeejp.laputa.lib_download;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class ProgressRequestBody extends RequestBody {
    private MediaType contentType;
    private File file;
    private DownloadListener listener;

    public ProgressRequestBody(MediaType contentType, File file, DownloadListener listener) {
        this.contentType = contentType;
        this.file = file;
        this.listener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() throws IOException {
        return super.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source;
        try {
            source = Okio.source(file);
            Buffer buf = new Buffer();
            long remaining = contentLength();
            long current = 0;
            for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                sink.write(buf, readCount);
                current += readCount;
                Log.e(DownLoadManager.TAG, "remaining------>" + remaining);
                Log.e(DownLoadManager.TAG, "current------>" + current);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
