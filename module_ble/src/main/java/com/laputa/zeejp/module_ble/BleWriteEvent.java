package com.laputa.zeejp.module_ble;

public class BleWriteEvent {

    private byte[] data;
    private int action;

    public BleWriteEvent() {
    }

    public BleWriteEvent(byte[] data) {
        this.data = data;
    }

    public BleWriteEvent(byte[] data, int action) {
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
