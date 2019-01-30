package com.laputa.zeejp.module_login.app;

import android.util.Log;

public class LUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.i("Crash", "thread = " + t.toString() + ",throwable = " + e.getMessage());
    }
}
