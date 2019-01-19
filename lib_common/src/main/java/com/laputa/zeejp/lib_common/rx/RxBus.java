package com.laputa.zeejp.lib_common.rx;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private final static String TAG = "RxBus";
    private final Subject<Object> mBus;

    public static RxBus newInstance() {
        return Holder.INSTANCE;
    }

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    public void post(Object object) {
        mBus.onNext(object);
    }

    private <T> Observable<T> toObservable(Class<T> clz) {
        return mBus.ofType(clz);
    }

    public <T> Disposable register(Class<T> clz, Consumer<T> onNext) {
        return toObservable(clz).subscribe(
                t -> {
                    try {
                        onNext.accept(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                , throwable -> Log.i(TAG, "throwable: " + throwable.getLocalizedMessage())
        );
    }

    public <T> Disposable register(Class<T> clz, Scheduler scheduler, Consumer<T> onNext) {
        return toObservable(clz)
                .observeOn(scheduler)
                .subscribe(
                        t -> {
                            try {
                                onNext.accept(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        , throwable -> Log.i(TAG, "throwable: " + throwable.getLocalizedMessage())
                );
    }

    public void unRegister(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void unRegister(Disposable... disposables) {
        for (Disposable dis : disposables) {
            unRegister(dis);
        }
    }

    public boolean hasObervers() {
        return mBus.hasObservers();
    }

    private final static class Holder {
        private static final RxBus INSTANCE = new RxBus();
    }
}
