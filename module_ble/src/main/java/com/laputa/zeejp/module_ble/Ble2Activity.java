package com.laputa.zeejp.module_ble;

import android.Manifest;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.laputa.zeejp.lib_common.rx.RxBus;
import com.laputa.zeejp.module_ble.databinding.ActivityBleBinding;
import com.polidea.rxandroidble2.RxBleDevice;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zeejp.laputa.lib_mvvm.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Ble2Activity extends BaseActivity<ActivityBleBinding> {

    private final String TAG = "Ble2Activity";
    private DeviceAdapter mAdapter;
    private Disposable register;
    private Disposable bleNotifyEventRegister;
    private Disposable bleWriteEventRegister;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ble;
    }

    @Override
    protected void initData() {
        super.initData();
        initRx();
        checkPermission();
    }

    private void initRx() {
        register = RxBus.newInstance().register
                (
                        BleConnectStateEvent.class
                        , AndroidSchedulers.mainThread()
                        , bleConnectStateEvent -> {
                            switch (bleConnectStateEvent.getStatus()) {
                                case BleConnectStateEvent.STATE_CONNECTED:
                                    showConnectedInfo("已连接!");
                                    break;
                                case BleConnectStateEvent.STATE_CONNECTEDING:
                                    showConnectedInfo("连接中... ...");
                                    break;
                                case BleConnectStateEvent.STATE_DISCONNECTED:
                                    showConnectedInfo("已断开连接!");
                                    break;
                                case BleConnectStateEvent.STATE_DISCONNECTEDING:
                                    showConnectedInfo("断开连接中......");
                                    break;
                                case BleConnectStateEvent.STATE_SERVICE_DISCOVERED:
                                    showConnectedInfo("服务发现!");
                                    break;
                                default:
                                    break;
                            }
                        }
                );

        bleNotifyEventRegister = RxBus.newInstance().register(
                BleNotifyEvent.class
                , AndroidSchedulers.mainThread()
                , bleNotifyEvent -> {
                    if (bleNotifyEvent.getAction()!=0){
                        byte[] data = bleNotifyEvent.getData();
                        showDataInfo("\n"+ HexString.bytesToHex(data));
                    }
                }
        );

        bleWriteEventRegister = RxBus.newInstance().register(
                BleWriteEvent.class
                , AndroidSchedulers.mainThread()
                , bleWriteEvent -> {
                    if (bleWriteEvent.getAction()!=0){
                        byte[] data = bleWriteEvent.getData();
                        showDataInfo("\n"+ HexString.bytesToHex(data));
                    }
                }
        );
    }

    @Override
    protected void initView() {
        super.initView();
        initRecyclerView();
    }

    @Override
    protected void onDestroy() {
        BleHelper.getInstance().exit();
        if (register != null && !register.isDisposed()) register.dispose();
        if (bleNotifyEventRegister != null && !bleNotifyEventRegister.isDisposed()) bleNotifyEventRegister.dispose();
        super.onDestroy();
    }

    private void initRecyclerView() {
        List<RxBleDevice> deviceList = new ArrayList<>();
        mAdapter = new DeviceAdapter(deviceList);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RxBleDevice rxBleDevice = mAdapter.getRxBleDevice(position);
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
                break;
            case R.id.tv_action_close:
                break;
        }
    }

    private void showErrorInfo(String msg) {
        mViewDataBing.tvInfo.setText(msg);
    }

    private void showDataInfo(String msg) {
        mViewDataBing.tvInfo.append(msg);
    }

    private void showConnectedInfo(String msg) {
        mViewDataBing.tvInfoDevice.setText(msg);
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

                            BleHelper.getInstance().startAutoConnect();

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


}
