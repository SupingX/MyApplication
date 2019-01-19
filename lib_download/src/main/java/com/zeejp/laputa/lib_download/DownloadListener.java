package com.zeejp.laputa.lib_download;

public interface DownloadListener {
    void onStart(String url);

    void onProgress(String url, int length, int maxLength);

    void onFinish(String msg);

    void onError(String msg);
}
