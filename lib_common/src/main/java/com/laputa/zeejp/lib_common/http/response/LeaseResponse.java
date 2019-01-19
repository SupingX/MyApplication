package com.laputa.zeejp.lib_common.http.response;

public class LeaseResponse {
    private boolean notice;
    private String time; //到期时间 2018-09-11

    public boolean isNotice() {
        return notice;
    }

    public void setNotice(boolean notice) {
        this.notice = notice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
