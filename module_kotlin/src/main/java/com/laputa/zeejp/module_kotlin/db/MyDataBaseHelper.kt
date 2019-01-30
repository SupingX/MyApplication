package com.laputa.zeejp.module_kotlin.db

import android.database.sqlite.SQLiteDatabase
import com.laputa.zeejp.module_kotlin.base.BaseApplication
import com.laputa.zeejp.module_kotlin.db.table.CityTable
import com.laputa.zeejp.module_kotlin.db.table.DayTable
import org.jetbrains.anko.db.*

class MyDataBaseHelper() : ManagedSQLiteOpenHelper(BaseApplication.instance(), DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_NAME = "test_db_study_kotlin"
        val DB_VERSION = 1
        val instance: MyDataBaseHelper by lazy { MyDataBaseHelper() } // instance 这个属性使用了 lazy 委托，它表示直到它真的被调用才会被创建
    }

    override fun onCreate(db: SQLiteDatabase) {
        /* db.createTable(
             CityTable.NAME
             ,true
             , Pair(CityTable.ID, INTEGER + PRIMARY_KEY)
             , Pair(CityTable.CITY, TEXT)
             , Pair(CityTable.COUNTRY, TEXT)
         )

         db.createTable(
                 DayTable.NAME
                 ,true
                 , Pair(DayTable.ID, INTEGER + PRIMARY_KEY)
                 , Pair(DayTable.DATE, INTEGER)
                 , Pair(DayTable.DESCRIPTION, TEXT)
                 , Pair(DayTable.HIGH, INTEGER)
                 , Pair(DayTable.LOW, INTEGER)
                 , Pair(DayTable.ICON_URL, TEXT)
                 , Pair(DayTable.CITY_ID, INTEGER)
         )*/

        db.createTable(
                CityTable.NAME,
                true,
                CityTable.ID to INTEGER + PRIMARY_KEY,
                CityTable.CITY to TEXT,
                CityTable.COUNTRY to TEXT
        )

        db.createTable(
                DayTable.NAME,
                true,
                DayTable.ID to INTEGER + PRIMARY_KEY,
                DayTable.DATE to INTEGER,
                DayTable.DESCRIPTION to TEXT,
                DayTable.HIGH to INTEGER,
                DayTable.LOW to INTEGER,
                DayTable.ICON_URL to TEXT,
                DayTable.CITY_ID to INTEGER
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(CityTable.NAME, true)
        db.dropTable(DayTable.NAME, true)
        onCreate(db)
    }

}
