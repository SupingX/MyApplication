package com.laputa.zeejp.module_ble;

import com.jakewharton.rx.ReplayingShare;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import util.SharedPreferenceUtil;

public class BleHelper2 {
    public static final String NAME = "NAME";
    private final String TAG = "BleHelper2";
    private final RxBleClient mRxBleClient;
    private String mDeviceMac;
    private String mDeviceName;
    private RxBleDevice mRxBleDevice;
    private RxBleConnection mRxBleConnection;
    private Observable<RxBleConnection> connectionObservable;
    // disposables
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BleHelper2() {
        SharedPreferenceUtil sp = new SharedPreferenceUtil(MyApplication.getInstance(), "test");
        mRxBleClient = RxBleClient.create(MyApplication.getInstance());
        mDeviceName = sp.getString(NAME);
    }

    public static BleHelper2 getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private final static BleHelper2 INSTANCE = new BleHelper2();
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
    }

    // 结束连接开关 disconnectTriggerSubject.onNext(true)
    private PublishSubject<Boolean> disconnectTriggerSubject = PublishSubject.create();

    private Observable<RxBleConnection> prepareConnectionObservable(RxBleDevice rxBleDevice) {
        return rxBleDevice
                .establishConnection(false)
                .takeUntil(disconnectTriggerSubject)
                .compose(ReplayingShare.instance());
    }

    private void connect(){
        if (connectionObservable == null) {
            return;
        }
        connectionObservable
                .flatMapSingle(RxBleConnection::discoverServices)
                .flatMapSingle(rxBleDeviceServices->rxBleDeviceServices.getCharacteristic(UUID.fromString(UUIDs.UUID_CHARACTERISTIC_NOTIFY)))

                ;
    }

    private void disconnect(){
        disconnectTriggerSubject.onNext(true);
    }




}
