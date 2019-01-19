package com.laputa.zeejp.module_ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.text.TextUtils;
import android.util.Log;

import com.laputa.zeejp.lib_common.rx.RxBus;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import util.SharedPreferenceUtil;

import static com.laputa.zeejp.module_ble.BleNotifyEvent.ACTION_01;
import static com.laputa.zeejp.module_ble.BleNotifyEvent.ACTION_04;
import static com.laputa.zeejp.module_ble.BleNotifyEvent.ACTION_06;

public class BleHelper {
    public static final String NAME = "NAME";
    private final String TAG = "BleHelper";
    private final String UUID_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    private final String UUID_CHARACTERISTIC_NOTIFY = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    private final String UUID_CHARACTERISTIC_WRITE = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    private final String UUID_DESC_CCC = "00002902-0000-1000-8000-00805f9b34fb";
    private final SharedPreferenceUtil sp;
    private final RxBleClient mRxBleClient;
    private RxBleConnection mRxBleConnection;
    private String mDeviceName = null;
    private RxBleDevice mRxBleDevice = null;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable autoConnectSubscribe;
    private Disposable startScanAutoConnectedDisposable;
    private Disposable startScanScanSubscription;
    private Disposable connectDeviceSubscribe;
    private Disposable deviceStateSubscribe;
    private Disposable setupNotificationSubscribe;

    private BleHelper() {
        sp = new SharedPreferenceUtil(MyApplication.getInstance(), "test");
        mRxBleClient = RxBleClient.create(MyApplication.getInstance());
        mDeviceName = sp.getString(NAME);
    }

    public static BleHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private final static BleHelper INSTANCE = new BleHelper();
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
    }

    private BleCallBack callBack;

    public void setCallBack(BleCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 根据当前保存的设备名称，自动打开搜索，再连接匹配的设备
     */
    public void startAutoConnect() {
        Log.i(TAG, "startAutoConnect: name = " + mDeviceName);
        dispose(autoConnectSubscribe, "autoConnectSubscribe 1");
        autoConnectSubscribe = Observable.interval(5, 30, TimeUnit.SECONDS)
                .filter(
                        along -> {
                            Log.i(TAG, "autoConnect: name =" + mDeviceName + ",isConnected = ");
                            return !TextUtils.isEmpty(mDeviceName)
//                                    && !(mRxBleDevice!=null && mRxBleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED)
                                    ;
                        }
                )
                .subscribe(
                        aLong -> startScanAutoConnected(mDeviceName)
                );
    }

    private void dispose(Disposable disposable, String log) {
        if (disposable != null && !disposable.isDisposed()) {
            Log.i(TAG, "dispose:  " + log);
            disposable.dispose();
            disposable = null;
        }
    }

    private Observable<ScanResult> getStartScan() {
        return mRxBleClient
                .scanBleDevices(new ScanSettings.Builder()
                                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                                .build()
                        // add filters if needed
                )
                .filter(scanResult -> {
                    String name = scanResult.getBleDevice().getName();
                    return !TextUtils.isEmpty(name);
                });
    }

    private Observable<RxBleConnection> getConnectDevice(RxBleDevice device) {
        return device
                .establishConnection(false)
                .doOnSubscribe(
                        subscribe -> {
                            stopScan();
                            mRxBleDevice = device;
                        }
                );
    }

    private void registerDeviceState(RxBleDevice rxBleDevice) {
        dispose(deviceStateSubscribe, "deviceStateSubscribe 1");
        Log.i(TAG, "registerDeviceState: " + rxBleDevice);
        deviceStateSubscribe = rxBleDevice
                .observeConnectionStateChanges()
                //.doOnNext(this::onConnectionStateChange)
                .subscribe(this::onConnectionStateChange);
    }

    private void onConnectionStateChange(RxBleConnection.RxBleConnectionState rxBleConnectionState) {
        Log.i(TAG, "onConnectionStateChange: " + rxBleConnectionState);
        if (rxBleConnectionState == RxBleConnection.RxBleConnectionState.CONNECTED) {
            dispose(autoConnectSubscribe, "autoConnectSubscribe 2");
            RxBus.newInstance().post(new BleConnectStateEvent(BleConnectStateEvent.STATE_CONNECTED));
            if (callBack != null) {
                callBack.onConnectedState(true);
            }
        } else if (rxBleConnectionState == RxBleConnection.RxBleConnectionState.DISCONNECTED) {
            dispose(deviceStateSubscribe, "deviceStateSubscribe 3");
            startAutoConnect();
            RxBus.newInstance().post(new BleConnectStateEvent(BleConnectStateEvent.STATE_DISCONNECTED));
            if (callBack != null) {
                callBack.onConnectedState(false);
            }
        } else if (rxBleConnectionState == RxBleConnection.RxBleConnectionState.CONNECTING) {
            RxBus.newInstance().post(new BleConnectStateEvent(BleConnectStateEvent.STATE_CONNECTEDING));
            if (callBack != null) {
                callBack.onConnectedState(false);
            }
        } else if (rxBleConnectionState == RxBleConnection.RxBleConnectionState.DISCONNECTING) {
            RxBus.newInstance().post(new BleConnectStateEvent(BleConnectStateEvent.STATE_DISCONNECTEDING));
            if (callBack != null) {
                callBack.onConnectedState(false);
            }
        }

    }

    private void stopScan() {
        dispose(startScanAutoConnectedDisposable, "startScanAutoConnectedDisposable 4");
        dispose(startScanScanSubscription, "startScanScanSubscription 5");
    }

    private void startScan(Consumer<ScanResult> onNext) {
        stopScan();
        startScanScanSubscription = getStartScan()
                .doFinally(() -> dispose(startScanScanSubscription, "startScanScanSubscription 6"))
                .subscribe(
                        onNext,
                        throwable -> {
                            // Handle an error here.
                            throwable.printStackTrace();
                            Log.i(TAG, "startScan error : " + throwable.getLocalizedMessage());
                        }
                );
    }

    private void startScanAutoConnected(String name) {
        Log.e(TAG, "__________________________________________________________________________");
        Log.i(TAG, "startScanAutoConnected: start ...");
        stopScan();
        dispose(startScanAutoConnectedDisposable, "startScanAutoConnectedDisposable 7");
        startScanAutoConnectedDisposable = mRxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
                // add filters if needed
        )
                .doFinally(() -> dispose(startScanAutoConnectedDisposable, "startScanAutoConnectedDisposable 8"))
                .filter(scanResult -> {
                    // Log.e(TAG, "startScanAutoConnected: " + scanResult.getBleDevice());
                    return !TextUtils.isEmpty(name) && name.equals(scanResult.getBleDevice().getName());
                })
                .firstElement()
                .subscribe(
                        scanResult -> {
                            // Process scan result here.
                            RxBleDevice rxBleDevice = DeviceUtil.parseData(scanResult);
                            if (rxBleDevice != null) {
                                Log.i(TAG, "startScanAutoConnected scanResult:  " + rxBleDevice);
                                Log.e(TAG, "startScanAutoConnected scanResult:  " + Arrays.toString(scanResult.getScanRecord().getBytes()));
                                registerDeviceState(rxBleDevice);
                                connectDevice(rxBleDevice);
                            }

                        },
                        throwable -> {
                            // Handle an error here.
                            throwable.printStackTrace();
                            Log.i(TAG, "startScanAutoConnected error : " + throwable.getLocalizedMessage());
                        }
                );

// When done, just dispose.
        //  scanSubscription.dispose();
    }

    /**
     * 连接设备
     */
    private void connectDevice(RxBleDevice rxBleDevice) {
        dispose(connectDeviceSubscribe, "connectDeviceSubscribe 9");
        connectDeviceSubscribe = getConnectDevice(rxBleDevice)
                .flatMapSingle(rxBleConnection ->
                        {
                            this.mRxBleConnection = rxBleConnection;
//                            return rxBleConnection.discoverServices(200, TimeUnit.MILLISECONDS);
                            return rxBleConnection.discoverServices();

                        }
                )
                //.take(1) // Disconnect automatically after discovery
//                .doFinally(() -> dispose(connectDeviceSubscribe))
                .flatMapSingle(BleHelper.this::checkService)
                .subscribe(BleHelper.this::connectSuccess, this::connectFail);


    }

    private void connectFail(Throwable throwable) {
        Log.i(TAG, "connectFail: " + throwable.getLocalizedMessage());
        throwable.printStackTrace();
        dispose(connectDeviceSubscribe, "connectDeviceSubscribe 10");
    }

    private void connectSuccess(Boolean aBoolean) {
        Log.i(TAG, "connectSuccess: ");
        setupNotification();
        RxBus.newInstance().post(new BleConnectStateEvent(BleConnectStateEvent.STATE_SERVICE_DISCOVERED));
        if (callBack != null) {
            callBack.onServiceDiscovered();
        }
    }

    private SingleSource<Boolean> checkService(RxBleDeviceServices rxBleDeviceServices) {
        return rxBleDeviceServices.getService(UUID.fromString(UUID_SERVICE))
                .flatMap(new Function<BluetoothGattService, SingleSource<Boolean>>() {
                    @Override
                    public SingleSource<Boolean> apply(BluetoothGattService bluetoothGattService) throws Exception {
                        BluetoothGattCharacteristic writeCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(UUID_CHARACTERISTIC_WRITE));
                        BluetoothGattCharacteristic notifyCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(UUID_CHARACTERISTIC_NOTIFY));
                        if (writeCharacteristic != null && notifyCharacteristic != null) {
                          /*  writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                            BluetoothGattDescriptor descriptor = notifyCharacteristic.getDescriptor(UUID.fromString(UUID_DESC_CCC));
                            if (descriptor != null) {
                                // descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                mRxBleConnection.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            }
                            setupNotification();*/
                            return Single.just(true);

                        }
                        return Single.just(false);
                    }
                });
    }


    public void write(byte[] data) {


        if (isConnected()) {
            Disposable subscribe = mRxBleConnection.writeCharacteristic(UUID.fromString(UUID_CHARACTERISTIC_WRITE), data)
                    .subscribe(new Consumer<byte[]>() {
                        @Override
                        public void accept(byte[] bytes) throws Exception {
                            Log.i(TAG, "write: " + HexString.bytesToHex(bytes));
                            RxBus.newInstance().post(new BleWriteEvent(bytes));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.i(TAG, "write: " + throwable.getLocalizedMessage());
                            throwable.printStackTrace();
                        }
                    });
        }

    }

    private boolean isConnected() {
        return mRxBleDevice != null && mRxBleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }


    private void setupNotification() {
        //if (isConnected()) {
        dispose(setupNotificationSubscribe, "setupNotificationSubscribe 11");
        //.observeOn(AndroidSchedulers.mainThread())
        setupNotificationSubscribe =

                mRxBleConnection
                        .setupNotification(UUID.fromString(UUID_CHARACTERISTIC_NOTIFY))
                        .flatMap(notificationObservable -> notificationObservable)

                        /*    Observable
                            .timer(500, TimeUnit.MILLISECONDS)
                            .flatMap(new Function<Long, ObservableSource<byte[]>>() {
                                @Override
                                public ObservableSource<byte[]> apply(Long aLong) throws Exception {

                                    return mRxBleConnection
                                            .setupNotification(UUID.fromString(UUID_CHARACTERISTIC_NOTIFY))
                                            .flatMap(notificationObservable -> notificationObservable);
                                }
                            })*/
                        //.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onNotificationReceived, this::onNotificationSetupFailure);

//        }
    }

    private void onNotificationSetupFailure(Throwable throwable) {
        Log.i(TAG, "setupNotification: " + throwable.getLocalizedMessage());
        throwable.printStackTrace();
        dispose(setupNotificationSubscribe, "setupNotificationSubscribe 12");

    }

    private void onNotificationReceived(byte[] bytes) {
        Log.i(TAG, "notify: " + HexString.bytesToHex(bytes));

        parseData(bytes);
    }

    private void parseData(byte[] bytes) {
        if (bytes == null || bytes.length < 6) {
            Log.i(TAG, "[parseData] data error");
            return;
        }
        if (bytes[0] == Protocol02.HEADER[0] && bytes[1] == Protocol02.HEADER[1] && Protocol02.checkCRC(bytes)) {
            int action = 0;
            switch (bytes[2]) {
                case Protocol02.NOTIFY_BG_DATA:
                    Log.i(TAG, "parseData: 收到数据");
                    action = ACTION_04;
                    write(Protocol02.getConfirmBgDataBytes());
                    break;
                case Protocol02.NOTIFY_BEGIN_WORK:
                    Log.i(TAG, "parseData: 确认工作");
                    action = ACTION_06;
                    write(Protocol02.getConfirmWithInit());
                    break;
                case Protocol02.NOTIFY_WORKING:
                    Log.i(TAG, "parseData: 已开始工作");
                    action = ACTION_01;
                    break;
            }

            RxBus.newInstance().post(new BleNotifyEvent(bytes, action));
        }

    }

    public void exit() {
        dispose(mCompositeDisposable, "exit 1");
        dispose(startScanAutoConnectedDisposable, "exit 2");
        dispose(autoConnectSubscribe, "exit 3");
        dispose(startScanScanSubscription, "exit 4");
        dispose(connectDeviceSubscribe, "exit 5");
        dispose(deviceStateSubscribe, "exit 6");
        dispose(setupNotificationSubscribe, "exit 7");
    }


}
