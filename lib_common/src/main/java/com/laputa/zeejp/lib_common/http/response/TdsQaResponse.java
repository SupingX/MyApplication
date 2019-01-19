package com.laputa.zeejp.lib_common.http.response;

/**
 * 水质评价
 */
public class TdsQaResponse {
    private String name;
    private int max;
    private int min;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }


}
