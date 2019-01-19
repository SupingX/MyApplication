package com.zeejp.laputa.lib_download;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownService {
    @Streaming
    @GET("/web/api/common/getFiles/{apkId}")
    Flowable<ResponseBody> downLoad(@Path("apkId") String apkId);


    @Streaming
    @GET
    Flowable<ResponseBody> downLoad2(@Url String url);
}
