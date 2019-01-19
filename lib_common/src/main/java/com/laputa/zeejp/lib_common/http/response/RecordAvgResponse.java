package com.laputa.zeejp.lib_common.http.response;

public class RecordAvgResponse {
    private double tdsAvg;
    private int waterQuantity;

    public double getTdsAvg() {
        return tdsAvg;
    }

    public void setTdsAvg(double tdsAvg) {
        this.tdsAvg = tdsAvg;
    }

    public int getWaterQuantity() {
        return waterQuantity;
    }

    public void setWaterQuantity(int waterQuantity) {
        this.waterQuantity = waterQuantity;
    }
}
