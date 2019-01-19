package com.laputa.zeejp.lib_common.rx;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

public class RxBus2 {

    private final FlowableProcessor<Object> mBus;

    public static RxBus2 newInstance(){
        return Holder.INSTANCE;
    }

    private RxBus2(){
        mBus = PublishProcessor.create().toSerialized();
    }

    public void post(Object object){
        mBus.onNext(object);
    }

    public <T>Flowable<T> toFlowable(Class<T> clz){
        return  mBus.ofType(clz);
    }

    public  boolean hasSubscribers(){
        return mBus.hasSubscribers();
    }

    private final static class Holder{
        private static final RxBus2 INSTANCE = new RxBus2();
    }
}
