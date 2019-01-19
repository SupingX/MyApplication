package com.laputa.zeejp.module_kotlin

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.laputa.zeejp.module_kotlin.adapter.ForecastAdapter
import com.laputa.zeejp.module_kotlin.test.lession_1.Animal
import com.laputa.zeejp.module_kotlin.test.lession_1.Any
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val items = listOf(
            "Mon 6/23 - Sunny - 31/17",
            "Tue 6/24 - Foggy - 21/8",
            "Wed 6/25 - Cloudy - 22/17",
            "Thurs 6/26 - Rainy - 18/11",
            "Fri 6/27 - Foggy - 21/10",
            "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
            "Sun 6/29 - Sunny - 20/7"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_hello.setTextColor(Color.RED)
        tv_hello.text = "hello kotlin"

        dev()
        initRecyclerView()
    }

    private fun initRecyclerView() {

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = ForecastAdapter(items)
         (recycler.adapter as ForecastAdapter).notifyDataSetChanged()
    }

    private fun dev() {
        var any = Any("hello")
        var animal = Animal("zeej", "ccc")
        var sum = animal.add(1, 3)
        val reduce = Animal("ccc",null).reduce(10, 100)
        tv_hello.append("/n$sum")
        tv_hello.append("/n$reduce")


    }
}
