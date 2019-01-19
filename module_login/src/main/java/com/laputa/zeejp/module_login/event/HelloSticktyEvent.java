package com.laputa.zeejp.module_login.event;

public class HelloSticktyEvent {
    private String hello;

    public HelloSticktyEvent(String hello) {
        this.hello = hello;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
