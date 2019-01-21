package com.laputa.zeejp.module_kotlin.domain

import com.laputa.zeejp.module_kotlin.domain.model.ForecastList
import com.laputa.zeejp.module_kotlin.domain.model.Forecast as ModelForecast // 使用了两个相同得类时，指定一个别名
import com.laputa.zeejp.module_kotlin.entity.data.Forecast
import com.laputa.zeejp.module_kotlin.entity.data.ForecastResult
import java.text.DateFormat
import java.util.*

public class ForecastDataMapper {
    fun converFromDataModel(forecast: ForecastResult): ForecastList {
        return ForecastList(forecast.city.name
                , forecast.city.country
                , convertForecastListToDomain(forecast.list))
    }

    private fun convertForecastListToDomain(list: List<Forecast>): List<ModelForecast> {
        return list.map { convertForecastItemToDomain(it) }
    }

    private fun convertForecastItemToDomain(it: Forecast): ModelForecast {
        return ModelForecast(
                convertDate(it.dt)
                , "${it.weather[0].description} ,speed:${it.speed} "
                , it.temp.max.toInt()
                , it.temp.min.toInt()
        )
    }

    private fun convertDate(date: Long): String {
        val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
        return df.format(date * 1000)

    }


}