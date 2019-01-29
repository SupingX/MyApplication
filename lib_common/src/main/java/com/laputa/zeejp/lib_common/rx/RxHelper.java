package com.laputa.zeejp.lib_common.rx;

import com.laputa.zeejp.lib_common.R;
import com.laputa.zeejp.lib_common.http.excption.ApiException;
import com.laputa.zeejp.lib_common.http.response.HttpResult;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {

    private static final String TAG = "RxUtil";

    /**
     * 统一线程处理
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 统一获取数据<T>
     */
    public static <T> FlowableTransformer<HttpResult<T>, T> parseData() {
        return upstream -> upstream
                .compose(checkTokenTimeOut())
                .flatMap(
                        (Function<HttpResult<T>, Flowable<T>>) tHttpResult -> {
                            if (tHttpResult.isSuccess()) {
                                return Flowable.just(tHttpResult.getData());
                            } else {
                                ApiException apiException = new ApiException(tHttpResult.getCode(), tHttpResult.getMsg());
                                return Flowable.error(apiException);
                            }
                        }
                )
                .compose(RxHelper.<T>rxSchedulerHelper())
                ;
    }

    /**
     * 统一获取数据Boolean
     */
    public static <T> FlowableTransformer<HttpResult<T>, Boolean> parseDataForBoolean() {
        return upstream -> upstream
                .compose(checkTokenTimeOut())
                .flatMap(
                        (Function<HttpResult<T>, Flowable<Boolean>>) tHttpResult -> {
                            try {
                                if (tHttpResult.isSuccess()) {
                                    return Flowable.just(true);
                                } else {
                                    ApiException apiException = new ApiException(tHttpResult.getCode(), tHttpResult.getMsg());
                                    return Flowable.error(apiException);
                                }
                            } catch (Exception e) {
                                return Flowable.error(e);
                            }
                        }
                );
                //.compose(rxSchedulerHelper());
    }

    public static <T> ObservableTransformer<HttpResult<T>, Boolean> parseDataForBoolean2() {
        return upstream -> upstream
                .flatMap(new Function<HttpResult<T>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> apply(HttpResult<T> tHttpResult) throws Exception {
                        try {
                            if (tHttpResult.isSuccess()) {
                                return Observable.just(true);
                            } else {
                                ApiException apiException = new ApiException(tHttpResult.getCode(), tHttpResult.getMsg());
                                return Observable.error(apiException);
                            }
                        } catch (Exception e) {
                            return Observable.error(e);
                        }
                    }
                });
        //.compose(rxSchedulerHelper());
    }

    /**
     * 验证token是否过期 必须用在解析之前
     */
    private static <T> FlowableTransformer<HttpResult<T>, HttpResult<T>> checkTokenTimeOut() {
        return upstream -> upstream
                .flatMap(
                        (Function<HttpResult<T>, Flowable<HttpResult<T>>>) tHttpResult -> {
                            boolean success = tHttpResult.isSuccess();
                            int code = tHttpResult.getCode();
                            if (!success && code == 2002) {

                            }
                            return Flowable.just(tHttpResult);

                        }
                );
    }

    /**
     * 生成Flowable
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(emitter -> {
            try {
                emitter.onNext(t);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }


}
