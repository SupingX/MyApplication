package com.laputa.zeejp.module_ble;

import android.Manifest;
import android.app.NotificationManager;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.laputa.zeejp.module_ble.databinding.ActivityBleBinding;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zeejp.laputa.lib_mvvm.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.plugins.RxJavaPlugins;
import util.SharedPreferenceUtil;

public class BleActivity extends BaseActivity<ActivityBleBinding> {
    private final String TAG = "RxBle";
    private RxBleClient rxBleClient;
    private DeviceAdapter mAdapter;
    private Disposable startScanScanSubscription;
    private Disposable deviceStateSubscribe;
    private Disposable connectDeviceDisposable;
    private SharedPreferenceUtil sp;
    private boolean isConnected = false;
    private Disposable autoConnectSubscribe;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ble;
    }

    @Override
    protected void initData() {
        super.initData();
        sp = new SharedPreferenceUtil(this, "test");

        initBleClient();
        autoConnect();
    }

    private void autoConnect() {
        String name = sp.getString(NAME);
        Log.i(TAG, "autoConnect: name = " + name);
        autoConnectSubscribe = Observable.interval(5,30, TimeUnit.SECONDS)
                .filter(
                        along -> {
                            Log.i(TAG, "autoConnect: name =" + name + ",isConnected = " + isConnected);
                            return !TextUtils.isEmpty(name) && !isConnected;
                        }
                )
                .subscribe(
                        aLong -> {

                            startScanAutoConnected(name);
                        }
                );
    }

    private void initBleClient() {
        RxBleClient.setLogLevel(RxBleLog.VERBOSE);
        RxJavaPlugins.setErrorHandler(throwable -> {
            if (throwable instanceof UndeliverableException && throwable.getCause() instanceof BleException) {
                Log.v("SampleApplication", "Suppressed UndeliverableException: " + throwable.toString());
                return; // ignore BleExceptions as they were surely delivered at least once
            }
            // add other custom handlers if needed
            throw new RuntimeException("Unexpected Throwable in RxJavaPlugins error handler", throwable);
        });
        rxBleClient = RxBleClient.create(getApplicationContext());

        rxBleClient = RxBleClient.create(this);
        observeStateChanges(rxBleClient);
    }

    @Override
    protected void initView() {
        super.initView();

        initRecyclerView();

    }

    @Override
    protected void onDestroy() {
        stopScan();
        stopDeviceState();
        stopConnect();
        stopAutoConnect();
        servicesDisposable.dispose();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void initRecyclerView() {
        List<RxBleDevice> deviceList = new ArrayList<>();
        mAdapter = new DeviceAdapter(deviceList);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RxBleDevice rxBleDevice = mAdapter.getRxBleDevice(position);
                registerDeviceState(rxBleDevice);
                connectDevice(rxBleDevice);
            }
        });


        mViewDataBing.recycler.setLayoutManager(new LinearLayoutManager(this));
        mViewDataBing.recycler.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_action_start_scan:
                checkPermission();
                break;
            case R.id.tv_action_stop_scan:
                stopScan();
                break;
            case R.id.tv_action_close:
                stopConnect();
                break;
        }
    }

    private void stopDeviceState() {
        Log.i(TAG, "stopDeviceState: ");
        if (deviceStateSubscribe != null) {
            deviceStateSubscribe.dispose();
            deviceStateSubscribe = null;
        }
    }

    private void stopConnect() {
        Log.i(TAG, "stopConnect: ");
        if (connectDeviceDisposable != null) {
            connectDeviceDisposable.dispose();
            connectDeviceDisposable = null;
        }
    }

    private void stopAutoConnect() {
        Log.i(TAG, "stopConnect: ");
        if (autoConnectSubscribe != null) {
            autoConnectSubscribe.dispose();
            autoConnectSubscribe = null;
        }
    }

    private void registerDeviceState(RxBleDevice rxBleDevice) {
        deviceStateSubscribe = rxBleDevice.observeConnectionStateChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnectionStateChange);
    }

    private void connectDevice(RxBleDevice device) {
//        String macAddress = "AA:BB:CC:DD:EE:FF";
//        RxBleDevice device = rxBleClient.getBleDevice(macAddress);

        stopScan();
        mRxBleDevice = device;
        connectDeviceDisposable = device.establishConnection(false)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(this::stopConnect)
                .subscribe(
                        this::onConnectionReceived, this::onConnectionReceivedFail
                );
    }

    private void onConnectionStateChange(RxBleConnection.RxBleConnectionState rxBleConnectionState) {
        Log.i(TAG, "onConnectionStateChange: " + rxBleConnectionState);
        mViewDataBing.tvInfoDevice.setText("" + rxBleConnectionState.toString());
        isConnected = rxBleConnectionState == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    private void onConnectionReceivedFail(Throwable throwable) {
        Log.i(TAG, "onConnectionReceivedFail: 连接失败 " + throwable.getLocalizedMessage());
        mViewDataBing.tvInfo.setText("连接失败：" + throwable.getLocalizedMessage());

        isConnected = false;
        mViewDataBing.tvInfoDevice.setText("" + RxBleConnection.RxBleConnectionState.DISCONNECTED);
    }

    private void onConnectionReceived(RxBleConnection connection) {
        //noinspection ConstantConditions
        Log.i(TAG, "onConnectionReceived: 连接成功");
        mViewDataBing.tvInfo.setText("连接成功！");
        discoverServices(connection);

    }

    private final CompositeDisposable servicesDisposable = new CompositeDisposable();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void discoverServices(RxBleConnection connection) {
        Log.i(TAG, "discoverServices: start...");
        Disposable subscribe = connection.discoverServices(10 * 1000, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
                //.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(rxBleDeviceServices -> discoverServicesSuccess(connection, rxBleDeviceServices), this::discoverServicesFail);
        servicesDisposable.add(subscribe);
    }

    private void discoverServicesFail(Throwable throwable) {
        Log.i(TAG, "discoverServicesFail:  " + throwable.getLocalizedMessage());
        showErrorInfo("discoverServicesFail:  " + throwable.getLocalizedMessage());
    }

    private void showErrorInfo(String msg) {
        mViewDataBing.tvInfo.setText(msg);
    }

    public String UUID_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    public String UUID_CHARACTERISTIC_NOTIFY = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    public String UUID_CHARACTERISTIC_WRITE = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    public String UUID_DESC_CCC = "00002902-0000-1000-8000-00805f9b34fb";

    private void discoverServicesSuccess(RxBleConnection connection, RxBleDeviceServices rxBleDeviceServices) {
        Disposable checkServiceSubscribe = rxBleDeviceServices.getService(UUID.fromString(UUID_SERVICE))
                .flatMap(new Function<BluetoothGattService, SingleSource<Boolean>>() {
                    @Override
                    public SingleSource<Boolean> apply(BluetoothGattService bluetoothGattService) throws Exception {
                        BluetoothGattCharacteristic writeCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(UUID_CHARACTERISTIC_WRITE));
                        BluetoothGattCharacteristic notifyCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(UUID_CHARACTERISTIC_NOTIFY));
                        if (writeCharacteristic != null && notifyCharacteristic != null) {
                            writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                            Disposable subscribe = connection.setupNotification(notifyCharacteristic).subscribe(by -> {
                            }, throwable -> {
                            });
                            compositeDisposable.add(subscribe);
                            return Single.just(true);
                        }
                        return Single.just(false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCompleteConnectedSuccess, this::onCompleteConnectFail);
        servicesDisposable.add(checkServiceSubscribe);
    }

    private void onCompleteConnectFail(Throwable throwable) {
        throwable.printStackTrace();
        showErrorInfo("onCompleteConnectFail :" + throwable.getLocalizedMessage());
    }


    private void onCompleteConnectedSuccess(Boolean aBoolean) {
        if (aBoolean) {
            mViewDataBing.tvInfoDevice.setText("（service）匹配成功");

            saveDevice();
            isConnected = true;

        } else {
            mViewDataBing.tvInfoDevice.setText("（service）匹配失败");
        }
    }

    private final String NAME = "NAME";

    private void saveDevice() {

        sp.putString(NAME, mRxBleDevice.getName());
    }

    private RxBleDevice mRxBleDevice;

    private void enable() {
      /*  if (isConnected(mRxBleDevice)) {
            final Disposable disposable = connectionObservable
                    .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUuid))
                    .doOnNext(notificationObservable -> runOnUiThread(this::notificationHasBeenSetUp))
                    .flatMap(notificationObservable -> notificationObservable)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onNotificationReceived, this::onNotificationSetupFailure);

            compositeDisposable.add(disposable);
        }*/
    }

    private boolean isConnected(RxBleDevice bleDevice) {
        return bleDevice != null && bleDevice.getConnectionState() == RxBleConnection.RxBleConnectionState.CONNECTED;
    }

    private void stopScan() {
        Log.i(TAG, "stopScan: ");
        if (isScanning()) {
            startScanScanSubscription.dispose();
        }


    }

    private void clear() {
        mAdapter.clearRxBleDevice();
    }

    private boolean isScanning() {
        return startScanScanSubscription != null;
    }

    private void checkPermission() {
        final RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !
                            Log.i(TAG, "checkPermission accept: ");
                            startScan();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            Log.i(TAG, "checkPermission shouldShowRequestPermissionRationale: ");
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            Log.i(TAG, "checkPermission Need to go to the settings: ");
                        }


                    }
                });

    }

    private void startScan() {
        clear();
        stopScan();
        startScanScanSubscription = rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
                // add filters if needed
        )
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(this::dispose)
                .subscribe(
                        scanResult -> {
                            // Process scan result here.

                            RxBleDevice rxBleDevice = DeviceUtil.parseData(scanResult);
                            if (rxBleDevice != null) {
                                Log.i(TAG, "startScan scanResult:  " + rxBleDevice);
                                Log.e(TAG, "startScan scanResult:  " + Arrays.toString(scanResult.getScanRecord().getBytes()));
                                mAdapter.addRxBleDevice(rxBleDevice);
                            }

                        },
                        throwable -> {
                            // Handle an error here.
                            throwable.printStackTrace();
                            mViewDataBing.tvInfo.setText("startScan error : " + throwable.getLocalizedMessage());
                        }
                );

// When done, just dispose.
        //  scanSubscription.dispose();
    }

    private void startScanAutoConnected(String name) {
        Log.i(TAG, "____________________________________________");
        Log.i(TAG, "startScanAutoConnected: start ...");
        stopScan();
        startScanScanSubscription = rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
                // add filters if needed
        )
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(this::dispose)
                .filter(new Predicate<ScanResult>() {
                    @Override
                    public boolean test(ScanResult scanResult) throws Exception {
                        Log.e(TAG, "startScanAutoConnected: " + scanResult.getBleDevice());
                        return !TextUtils.isEmpty(scanResult.getBleDevice().getName()) && scanResult.getBleDevice().getName().equals(name);
                    }
                })
                .firstElement()
                .subscribe(
                        scanResult -> {
                            // Process scan result here.

                            RxBleDevice rxBleDevice = DeviceUtil.parseData(scanResult);
                            if (rxBleDevice != null) {
                                Log.i(TAG, "startScanAutoConnected scanResult:  " + rxBleDevice);
                                Log.e(TAG, "startScanAutoConnected scanResult:  " + Arrays.toString(scanResult.getScanRecord().getBytes()));
                                connectDevice(rxBleDevice);
                            }

                        },
                        throwable -> {
                            // Handle an error here.
                            throwable.printStackTrace();
                            mViewDataBing.tvInfo.setText("startScanAutoConnected error : " + throwable.getLocalizedMessage());
                        }
                );

// When done, just dispose.
        //  scanSubscription.dispose();
    }

    private void dispose() {
        Log.i(TAG, "startScan scanResult:  dispose");
        startScanScanSubscription = null;
    }

    private void observeStateChanges(RxBleClient client) {

        Disposable flowDisposable = client.observeStateChanges()
                .switchMap(state -> { // switchMap makes sure that if the state will change the rxBleClient.scanBleDevices() will dispose and thus end the scan
                    switch (state) {
                        case READY:
                            Log.i(TAG, "observeStateChanges READY:  ");
                            // everything should work
                            return client.scanBleDevices();
                        case BLUETOOTH_NOT_AVAILABLE:
                            Log.i(TAG, "observeStateChanges BLUETOOTH_NOT_AVAILABLE:  ");
                            // basically no functionality will work here
                        case LOCATION_PERMISSION_NOT_GRANTED:
                            Log.i(TAG, "observeStateChanges LOCATION_PERMISSION_NOT_GRANTED:  ");
                            // scanning and connecting will not work
                        case BLUETOOTH_NOT_ENABLED:
                            Log.i(TAG, "observeStateChanges BLUETOOTH_NOT_ENABLED:  ");
                            // scanning and connecting will not work
                        case LOCATION_SERVICES_NOT_ENABLED:
                            Log.i(TAG, "observeStateChanges LOCATION_SERVICES_NOT_ENABLED:  ");
                            // scanning will not work
                        default:
                            Log.i(TAG, "observeStateChanges empty:  ");
                            return Observable.empty();
                    }
                })
                .subscribe(
                        rxBleScanResult -> {
                            Log.i(TAG, "observeStateChanges rxBleScanResult:  " + rxBleScanResult);
                            // Process scan result here.
                          /*  RxBleDevice bleDevice = rxBleScanResult.getBleDevice();
                            if (!TextUtils.isEmpty(bleDevice.getName()) && !TextUtils.isEmpty(bleDevice.getMacAddress())) {
                                deviceList.add(bleDevice);
                                mAdapter.notifyDataSetChanged();
                            }*/

                        },
                        throwable -> {
                            // Handle an error here.
                            mViewDataBing.tvInfo.setText("observeStateChanges error : " + throwable.getLocalizedMessage());
                        }
                );

// When done, just dispose.
//        flowDisposable.dispose();
    }
}
