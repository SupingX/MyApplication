package com.laputa.zeejp.module_kotlin.domain.model

data class Forecast
(
        val date: String
        , val description: String
        , val high: Int
        , val low: Int
        , val icon: String
)