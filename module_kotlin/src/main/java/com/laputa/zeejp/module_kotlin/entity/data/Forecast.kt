package com.laputa.zeejp.module_kotlin.entity.data

data class Forecast
(
        val dt:Long
        ,val temp :Temperature
        ,val pressure:Float
        ,val humidity:Int
        ,val weather:List<Weather>
        ,val speed:Float
        ,val deg:Int
        ,val clouds:Int
        ,val rain:Float
)