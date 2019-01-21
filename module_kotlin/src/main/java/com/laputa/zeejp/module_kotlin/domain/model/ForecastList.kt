package com.laputa.zeejp.module_kotlin.domain.model

import com.laputa.zeejp.module_kotlin.domain.model.Forecast

data class ForecastList
(
        val city: String
        , val country: String
        , val dailyForecast: List<Forecast>
)