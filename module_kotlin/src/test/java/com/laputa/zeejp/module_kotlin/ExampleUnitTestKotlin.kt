package com.laputa.zeejp.module_kotlin

import com.laputa.zeejp.module_kotlin.entity.data.Coordinates
import com.laputa.zeejp.module_kotlin.entity.data.Forecast
import com.laputa.zeejp.module_kotlin.test.lession_1.Animal
import com.laputa.zeejp.module_kotlin.test.lession_1.Persion
import com.laputa.zeejp.module_kotlin.test.lession_2.Book
import com.laputa.zeejp.module_kotlin.entity.data.ForecastTest
import com.laputa.zeejp.module_kotlin.test.lession_2.myClip
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTestKotlin {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun lesson_1() {

        // 初体验

        val TAG = "lesson_1"
        val any = com.laputa.zeejp.module_kotlin.test.lession_1.Any("hello")
        log("any$any", TAG)

        val animal = Animal("zeej", "ccc")
        val sum = animal.add(1, 3)
        log("sum=$sum", TAG)

        val reduce = Animal("ccc", null).reduce(10, 100)
        log("reduce=$reduce", TAG)

        val person = Persion("zzz", 1, null)
        val result = person.sayHi()
        log("msg->$result", TAG)
        log("msg->${person.sayHi()}", TAG)
        log("msg->$person.sayHi()", TAG)
    }

    @Test
    fun lesson_2() {
        // 64
        val TAG = "lesson_2"
        // 基本类型
        val i1: Int = 7
        val i2 = 7
        val i3: Double = 7.1
        val i4 = 7.1

        val c1: Char = 'c'
        val c2: Char = 'c'
        val c3 = c1.toInt()
        //val c4: Char = 4 字符不能直接作为一个数字来处理

        val b1: Boolean = true
        val b2 = b1 or b1
        val b3 = b1 and b2

        val str1 = "Hello world"
        val c = str1[6]
        for (c2 in str1) {
            log(c2.toString(), TAG)
        }

        val book = Book("haha")
        book.name = "dans"
        val name = book.name
        log("$name:${book.author}", TAG)

        val myClip = name.myClip(name)
        log(myClip, TAG)

        val f1 = ForecastTest(Date(), 27.5f, "sunday")
        log(f1.toString(), TAG)
        val f2 = f1.copy(temperature = 33f)
        log(f2.toString(), TAG)

        // 映射对象到变量中
        val (val1, val2, val3) = f1

        log("val1=$val1", TAG)
        log("val2=$val2", TAG)
        log("val3=$val3", TAG)

        val coordinates = Coordinates(1.0f, 2.0f)
        log("lat : ${coordinates.lat} , lon : ${coordinates.lon}", TAG)

        // 【总结】
        // 1.基本类型 万物姐对象
        // 2.变量val var
        // 3.anko
        // 4.扩展函数 方法 + 属性
        // 5.async （anko）
        // 6.数据类data calss
        // 7.复制数据类 copy
        // 8.映射对象到变量中
        // 9.with函数
        // 10.操作符

        // 【问题】
        // 1.ForecastTest为什么不能获取属性
        // 2.data class 和 class的区别
        // 3.扩展函数名字一样在用一个文件里怎么引用
        // 4.类名一样在同一个文件里怎么引用
        // -在import用别名as

    }

    fun log(msg: String, tag: String) {
        System.out.println("[$tag] $msg")
    }
}
