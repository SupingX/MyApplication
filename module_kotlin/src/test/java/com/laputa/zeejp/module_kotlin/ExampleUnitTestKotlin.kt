package com.laputa.zeejp.module_kotlin

import com.laputa.zeejp.module_kotlin.R.id.tv_hello
import com.laputa.zeejp.module_kotlin.test.lession_1.Animal
import com.laputa.zeejp.module_kotlin.test.lession_1.Persion
import org.junit.Test

import org.junit.Assert.*

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

        val persion = Persion("zzz", 1, null)
        val result = persion.sayHi()
        log("msg->$result", TAG)
        log("msg->${persion.sayHi()}", TAG)
        log("msg->$persion.sayHi()", TAG)
    }

    @Test
    fun lesson_2() {
        // 基本类型
    }

    fun log(msg: String, tag: String) {
        System.out.println("[$tag] $msg")
    }
}
