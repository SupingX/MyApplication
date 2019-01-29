package com.laputa.zeejp.module_kotlin.util

import android.os.Build
import android.util.Log
import android.widget.Toast
import com.laputa.zeejp.module_kotlin.base.BaseApplication

public inline fun supportsLollipop(code: () -> Int) {
    //if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
    if (true) {
//        code()
        val code1: Int = code()
//        Log.i("TEST", "code=$code1")
        System.out.println("code = $code1");
    }
}

public inline fun supportsLollipop02(code: (a1: Int, a2: Int) -> Int) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
        val a1 = 11
        val a2 = 22
        val code1: Int = code(a1, a2)
        Log.i("TEST", "code=$code1")
    }
}

