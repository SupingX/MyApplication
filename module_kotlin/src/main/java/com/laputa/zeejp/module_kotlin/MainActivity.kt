package com.laputa.zeejp.module_kotlin

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.laputa.zeejp.module_kotlin.adapter.ForecastAdapter
import com.laputa.zeejp.module_kotlin.adapter.ForecastAdvance03Adapter
import com.laputa.zeejp.module_kotlin.adapter.ForecastAdvance04Adapter
import com.laputa.zeejp.module_kotlin.adapter.ForecastAdvanceAdapter
import com.laputa.zeejp.module_kotlin.domain.RequestForecastCommand
import com.laputa.zeejp.module_kotlin.domain.interfaces.OnItemClickListener
import com.laputa.zeejp.module_kotlin.domain.model.Forecast
import com.laputa.zeejp.module_kotlin.test.lession_1.Animal
import com.laputa.zeejp.module_kotlin.test.lession_1.Any
import com.laputa.zeejp.module_kotlin.test.lession_2.Request
import com.laputa.zeejp.module_kotlin.test.lession_2.mytext
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

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
        tv_hello.mytext = "test 扩展函数 属性"
//        print(tv_hello.mytext)
        toast(tv_hello.mytext)

        dev()
        initRecyclerView()

        // val reycler = this.find<RecyclerView>(R.id.recycler)

//        mytoast("hello kotlin!")
//
//        toast("haha in anko")
//
//        toast(R.string.app_name)
//        longToast(R.string.app_name)

        click.setOnClickListener {
            //getWeather()

            //getWeatherAdvance()

            //getWeatherAdvance03()

            view->getWeatherAdvance04()
        }

    }

    private fun getWeatherAdvance() {
        async {
            val result = RequestForecastCommand("94043").execute()
            uiThread {
                recycler.adapter = ForecastAdvanceAdapter(result)
            }
        }

    }

    private fun getWeatherAdvance03() {
        async {
            val result = RequestForecastCommand("94043").execute()
            uiThread {
                recycler.adapter = ForecastAdvance03Adapter(
                        result,
                        object : OnItemClickListener {
                            override fun invoke(forecast: Forecast) {
                                toast(forecast.toString())
                            }
                        }
                )
            }
        }
    }



    private fun getWeatherAdvance04() {



        val asyncResult = asyncResult {

        }


        val async = async {
            val result = RequestForecastCommand("94043").execute()
            uiThread {
                // 写在参数外面
                recycler.adapter = ForecastAdvance04Adapter(result) { forecast ->
                    toast(forecast.toString())
                }

                // 写在参数里面
                /*  recycler.adapter = ForecastAdvance04Adapter(result, { forecast ->
                      toast(forecast.toString())
                  })*/


            }
        }
    }


    private fun getWeather() {

        async {
            val url = "https://www.baidu.com/"
            val run = Request(url).run()
            uiThread {
                longToast(run)
                click.setTextColor(Color.RED)
            }
        }
    }

    private fun initRecyclerView() {

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler.adapter = ForecastAdapter(items)
        (recycler.adapter as ForecastAdapter).notifyDataSetChanged()
    }

    private fun dev() {
        var any = Any("hello")
        var animal = Animal("zeej", "ccc")
        var sum = animal.add(1, 3)
        val reduce = Animal("ccc", null).reduce(10, 100)
        tv_hello.append("/n$sum")
        tv_hello.append("/n$reduce")


    }
}
