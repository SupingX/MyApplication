package com.zeejp.laputa.lib_download;

import android.os.Environment;
import android.util.Log;

import com.laputa.zeejp.lib_common.http.ApiService;
import com.laputa.zeejp.lib_common.rx.RxHelper;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownLoadManager {

    public static final String TAG = "DownLoadManager";
    private final boolean DEBUG = true;
    private final OkHttpClient okHttpClient;
    private final Retrofit retrofit;

    private DownLoadManager() {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.HOST)
                .build();

    }

    public static DownLoadManager newInstance() {
        return Holder.INSTANCE;
    }

    public void download(String url, String path, DownloadListener listener) {
        DownService downService = retrofit.create(DownService.class);
        Disposable qinyuan = downService.downLoad2(url)
                .compose(RxHelper.rxSchedulerHelper())
//                .subscribeWith(new ResourceSubscriber<ResponseBody>() {
//                    @Override
//                    public void onNext(ResponseBody responseBody) {
//                        Log.i(TAG, "onNext: responseBody" + responseBody.toString());
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        Log.i(TAG, "accept: responseBody" + responseBody);

                        File qinyuan = createFile("qinyuan");

                        BufferedSink sink = Okio.buffer(Okio.sink(qinyuan));
                        Buffer buffer = sink.buffer();

                        long total = 0;
                        long len;
                        int bufferSize = 200 * 1024; //200kb

                        BufferedSource source = responseBody.source();
                        while ((len = source.read(buffer, bufferSize)) != -1) {
                            sink.emit();
                            total += len;
                            Log.i(DownLoadManager.TAG, "total: " + total);
                        }
                        source.close();
                        sink.close();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: throwable" + throwable);
                    }
                });
    }


    private File createFile(String name) {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                , "QinYuan");
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                return null;
//            }
//        }
        String tempFilePath = mediaStorageDir.getPath() + File.separator + name + ".apk";
        return new File(tempFilePath);
    }

    private final static class Holder {
        private static final DownLoadManager INSTANCE = new DownLoadManager();
    }
}
