package com.laputa.zeejp.lib_common.rx;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 RxRelay

 Relays are RxJava types which are both an Observable and a Consumer.

 Basically: A Subject except without the ability to call onComplete or onError.

 Subjects are useful to bridge the gap between non-Rx APIs. However, they are stateful in a damaging way: when they receive an onComplete or onError they no longer become usable for moving data. This is the observable contract and sometimes it is the desired behavior. Most times it is not.

 Relays are simply Subjects without the aforementioned property. They allow you to bridge non-Rx APIs into Rx easily, and without the worry of accidentally triggering a terminal state.

 As more of your code moves to reactive, the need for Subjects and Relays should diminish. In the transitional period, or for quickly adapting a non-Rx API, Relays provide the convenience of Subjects without the worry of the statefulness of terminal event behavior.

 */
public class RxBus4 {

    private Relay<Object> bus = null;
    private final Map<Class<?>, Object> mStickyEventMap;

    public static RxBus4 newInstance() {
        return Holder.INSTANCE;
    }

    private RxBus4() {
        bus = PublishRelay.create().toSerialized();
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    private final static class Holder {
        private static final RxBus4 INSTANCE = new RxBus4();
    }

    public void post(Object event) {
        bus.accept(event);
    }

    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        bus.accept(event);
    }

    public <T> Observable<T> tObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    public <T> Observable<T> tObservableSticky(Class<T> eventType) {
        synchronized (mStickyEventMap){
            Observable<T> tObservable = bus.ofType(eventType);
            Object event = mStickyEventMap.get(eventType);
            if (event != null) {
                return tObservable.mergeWith(Observable.create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                        emitter.onNext(eventType.cast(event));
                    }
                }));
            }else{
                return tObservable;
            }
        }
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

    public <T> Disposable registerSticky(Class<T> eventType, Consumer<T> onNext) {
        return tObservable(eventType).subscribe(onNext);
    }

    public void unRegister(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public <T> T  removeStickyEvent(Class<T> eventType){
        synchronized (mStickyEventMap){
            return eventType.cast((mStickyEventMap.remove(eventType)));
        }
    }

    public void removeAllStickyEvents(){
        synchronized (mStickyEventMap){
            mStickyEventMap.clear();
        }
    }





}
