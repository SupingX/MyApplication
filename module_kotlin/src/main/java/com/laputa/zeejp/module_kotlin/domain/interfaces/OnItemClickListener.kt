package com.laputa.zeejp.module_kotlin.domain.interfaces

import com.laputa.zeejp.module_kotlin.domain.model.Forecast

public interface OnItemClickListener {
    operator fun invoke(forecast: Forecast)
}