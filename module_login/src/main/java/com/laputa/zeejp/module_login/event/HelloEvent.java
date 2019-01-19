package com.laputa.zeejp.module_login.event;

public class HelloEvent {
    private String hello;

    public HelloEvent(String hello) {
        this.hello = hello;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
