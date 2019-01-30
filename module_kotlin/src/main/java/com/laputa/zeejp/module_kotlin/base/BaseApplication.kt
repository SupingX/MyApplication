package com.laputa.zeejp.module_kotlin.base

import android.app.Application
import android.database.sqlite.SQLiteOpenHelper
import com.laputa.zeejp.module_kotlin.test.lesson_5.TestWeiTuo
import okhttp3.internal.Internal.instance
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

class BaseApplication : Application() {

//    val database:SQLiteOpenHelper by lazy {
//        MyDataBaseHelper(applicationContext)
//    }

    val user : String by lazy(LazyThreadSafetyMode.NONE, {
        Companion.getUser()
    })



    companion object {
//        private var instance: Application? = null
        private var instance: Application by Delegates.notNull<Application>()
        fun instance()= instance!!

        private fun getUser(): String {

            return "123"
        }




    }





    override fun onCreate() {
        super.onCreate()
        instance = this
//        val db = database.writableDatabase
    }
}