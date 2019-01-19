package com.laputa.zeejp.lib_common.net;

import com.laputa.zeejp.lib_common.http.ApiService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceManager {

    private boolean DEBUG = true;

    private OkHttpClient defaultOkHttpClient;
    private Retrofit defaultRetrofit;

    private ApiServiceManager() {
        defaultOkHttpClient = createOkHttpClientBuilder().build();
        defaultRetrofit = createRetrofitBuilder(defaultOkHttpClient, ApiService.HOST).build();
    }

    private OkHttpClient.Builder createOkHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
    }

    private Retrofit.Builder createRetrofitBuilder(OkHttpClient okHttpClient, String baseUrl) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);
    }

    public <T> T createService(Class<T> service) {
        return defaultRetrofit.create(service);
    }

    public <T> T createService( Retrofit retrofit,Class<T> service) {
        return retrofit.create(service);
    }

    public <T> T createService( OkHttpClient okHttpClient,String baseUrl,Class<T> service) {
        return createRetrofitBuilder(okHttpClient,baseUrl).build().create(service);
    }

    public ApiService createApiService() {
        return createService(ApiService.class);
    }

    public static ApiServiceManager newInstance() {
        return Holder.INSTANCE;
    }

    private final static class Holder {
        static final ApiServiceManager INSTANCE = new ApiServiceManager();
    }
}
