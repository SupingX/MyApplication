package com.laputa.zeejp.module_kotlin.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.laputa.zeejp.module_kotlin.R
import com.laputa.zeejp.module_kotlin.domain.model.ForecastList
import kotlinx.android.synthetic.main.item_forecast.view.*

class ForecastAdvanceAdapter(val weekForecast: ForecastList) : RecyclerView.Adapter<ForecastAdvanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_forecast, null, false)
        return ForecastAdvanceAdapter.ViewHolder(view.tv_text)
    }


    override fun getItemCount(): Int = weekForecast.dailyForecast.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        with(weekForecast.dailyForecast[p1]) {
            p0.textView.text = "$date - $description - $high/$low "
            p0.textView.setTextColor(Color.RED)
        }
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}