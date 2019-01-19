package com.laputa.zeejp.module_ble;

public class BleConnectStateEvent {
    public static final int STATE_CONNECTED             = 0b0000001 ;
    public static final int STATE_CONNECTEDING          = 0b0000010 ;
    public static final int STATE_DISCONNECTED          = 0b0000100 ;
    public static final int STATE_DISCONNECTEDING       = 0b0001000 ;
    public static final int STATE_SERVICE_DISCOVERED    = 0b0010000 ;
    private int status;

    public BleConnectStateEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
