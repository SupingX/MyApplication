package com.laputa.zeejp.module_ble;

public class BleNotifyEvent {
    public static final int ACTION_06 = 0x0001;
    public static final int ACTION_04 = 0x0010;
    public static final int ACTION_01 = 0x0100;
    private byte[] data;
    private int action;

    public BleNotifyEvent() {
    }

    public BleNotifyEvent(byte[] data, int action) {
        this.data = data;
        this.action = action;
    }

    public BleNotifyEvent(byte[] data, int action,int type) {
        this.data = data;
        this.action = action;

    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }


}
