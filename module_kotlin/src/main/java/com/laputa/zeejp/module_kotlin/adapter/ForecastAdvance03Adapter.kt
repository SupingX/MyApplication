package com.laputa.zeejp.module_kotlin.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.laputa.zeejp.module_kotlin.R
import com.laputa.zeejp.module_kotlin.domain.interfaces.OnItemClickListener
import com.laputa.zeejp.module_kotlin.domain.model.Forecast
import com.laputa.zeejp.module_kotlin.domain.model.ForecastList
import org.jetbrains.anko.find

class ForecastAdvance03Adapter(val weekForecast: ForecastList, val onItemClick: OnItemClickListener) : RecyclerView.Adapter<ForecastAdvance03Adapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_forecast_03, null, false)
        return ForecastAdvance03Adapter.ViewHolder(view, onItemClick)
    }

    override fun getItemCount() = size()

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindData(get(p1))
    }

    class ViewHolder(val view: View, val itemClick: OnItemClickListener) : RecyclerView.ViewHolder(view) {
        private val iconView: ImageView = view.find(R.id.icon)
        private val dateView: TextView = view.find(R.id.date)
        private val descriptionView: TextView = view.find(R.id.description)
        private val maxTemperatureView: TextView = view.find(R.id.maxTemperature)
        private val minTemperatureView: TextView = view.find(R.id.minTemperature)

        fun bindData(forecast: Forecast) {
            with(forecast) {
                Log.d("item", "item = $this ")
                Glide.with(view.context).load(icon).into(iconView)
                dateView.text = date
                descriptionView.text = description
                maxTemperatureView.text = "$high ~"
                minTemperatureView.text = "$low"

                dateView.setTextColor(Color.WHITE)
                descriptionView.setTextColor(Color.WHITE)
                maxTemperatureView.setTextColor(Color.WHITE)
                minTemperatureView.setTextColor(Color.WHITE)

                itemView.setOnClickListener{
                    itemClick(forecast)
                    //itemClick.invoke(forecast)
                }
            }
        }

        //fun getIconUrl(icon: String): String = "http://openweathermap.org/img/w/$icon.png"

    }

    fun size() = weekForecast.dailyForecast.size

    operator fun get(position: Int): Forecast = weekForecast.dailyForecast[position]

    //fun getIconUrl(icon: String): String = "http://openweathermap.org/img/w/$icon.png" //内部类怎么调用
}