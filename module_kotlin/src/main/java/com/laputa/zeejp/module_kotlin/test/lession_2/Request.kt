package com.laputa.zeejp.module_kotlin.test.lession_2

import android.util.Log
import java.net.URL

public class Request(val url: String) {
    public fun run(): String {
        val forecastJsonStr = URL(url).readText() // 标准库中得扩展函数。小得响应
        Log.d(javaClass.simpleName, forecastJsonStr)
        return forecastJsonStr
    }


}