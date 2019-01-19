package com.laputa.zeejp.lib_common.rx;

import android.util.Log;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.Observer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * RxBus
 * RxJava的操作符在链式调用中一旦有一个抛出了异常，Observer就会直接执行onError方法，从而导致整个链式调用的结束。
 * <p>
 * Jake Wharton 大神的RxRelay出现异常也不会中止订阅关系。
 * <p>
 * RxRelay中的各个Relay既是Obserable类型，也是Consumer类型，他们是一个没有onComplete和onError的Subject。所以不用担心下游触发中止状态。
 */
public class RxBus3 {
    private final static String TAG = "RxBus3";
    private Relay<Object> bus = null;

    public static RxBus3 newInstance() {
        return Holder.INSTANCE;
    }

    private RxBus3() {
        bus = PublishRelay.create().toSerialized();
    }

    private final static class Holder {
        private static final RxBus3 INSTANCE = new RxBus3();
    }

    public void post(Object event) {
        bus.accept(event);
    }

    public <T> Observable<T> tObservable(Class<T> eventType) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                Observable<T> orgObservable = bus.ofType(eventType);
                orgObservable.subscribe(new io.reactivex.Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        emitter.onNext(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
            }
        });
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }

    /**
     * 最简单的封装
     */
    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext) {
        return tObservable(eventType).subscribe(onNext);
    }

    public void unRegister(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
