package com.laputa.zeejp.module_kotlin.domain

import com.laputa.zeejp.module_kotlin.domain.model.ForecastList
import com.laputa.zeejp.module_kotlin.test.lession_2.ForecastRequest

class RequestForecastCommand(val zipCode: String) : Command<ForecastList> {
    override fun execute(): ForecastList {
        val forecastRequest = ForecastRequest(zipCode)
        return ForecastDataMapper().converFromDataModel(forecastRequest.execute())
    }
}