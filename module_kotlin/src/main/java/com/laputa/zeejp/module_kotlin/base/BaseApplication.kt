package com.laputa.zeejp.module_kotlin.base

import android.app.Application

class BaseApplication : Application() {
    companion object {
        private var instance: Application? = null
        fun instance()= instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}