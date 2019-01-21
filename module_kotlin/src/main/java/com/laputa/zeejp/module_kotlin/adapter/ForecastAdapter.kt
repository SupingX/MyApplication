package com.laputa.zeejp.module_kotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.laputa.zeejp.module_kotlin.R
import kotlinx.android.synthetic.main.item_forecast.view.*

class ForecastAdapter(private val dataList: List<String>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_forecast, null, false)
        return ViewHolder(view.tv_text)
    }

    override fun getItemCount(): Int =dataList.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.view.tv_text.text = dataList[p1]
    }

    fun clear(){
        
    }

    class ViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

}
