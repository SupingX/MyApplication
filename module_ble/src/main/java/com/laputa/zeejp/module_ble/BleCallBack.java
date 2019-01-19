package com.laputa.zeejp.module_ble;

public interface BleCallBack {

    void onServiceDiscovered();

    void onConnectedState(boolean connected);

}
