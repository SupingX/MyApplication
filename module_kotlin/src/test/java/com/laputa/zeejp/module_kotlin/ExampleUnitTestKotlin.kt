package com.laputa.zeejp.module_kotlin

import com.laputa.zeejp.module_kotlin.entity.data.Coordinates
import com.laputa.zeejp.module_kotlin.test.lession_1.Animal
import com.laputa.zeejp.module_kotlin.test.lession_1.Persion
import com.laputa.zeejp.module_kotlin.test.lession_2.Book
import com.laputa.zeejp.module_kotlin.entity.data.ForecastTest
import com.laputa.zeejp.module_kotlin.test.lession_2.myClip
import com.laputa.zeejp.module_kotlin.util.supportsLollipop
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

    @Test
    fun lesson_3() {
        val TAG = "lesson_3"

        // 扩展函数

        // lambda
//        testLambda { hello ->
//            log("testLambda : $hello lambda", TAG)
//        }

        // 使用的时候
        testLambda({ hello ->test(hello) })

        // 内联函数
        supportsLollipop {
            log("", "supportsLollipop")
            add(1, 2)
        }


    }

    private fun test(hello: String): String {
        log("testLambda : $hello lambda", "lesson_3")
        return "haha"
    }

    @Test
    fun lesson_4() {
        doWhat { hello() }
        doWhat { hello("leng") }
        doWhat { hello("leng", "leng") }

        doLove ({
            love("leng", "leng")
        })

        doLove2 ({ name, nick -> love(name, nick) })
    }

    fun hello() {
        System.out.println("hello1 leng")
    }

    fun hello(name: String) {
        System.out.println("hello2 $name")
    }

    fun hello(name: String, nick: String) {
        System.out.println("hello3 $name - $nick")
    }

    fun love(name: String, nick: String): Int {
        System.out.println("hello4 $name - $nick")
        return 520
    }

    fun doWhat(what: () -> Unit) {
        what()
    }

    fun doLove(love: () -> Int) {
        val love1 = love()
        System.out.println("just doLove $love1")
    }

    fun doLove2(love: (name: String, nick: String) -> Int) {
        val name = "leng"
        val nick = "leng"
        val love1 = love(name, nick)
        System.out.println("just doLove2 $love1")
    }


    fun add(x: Int, y: Int): Int = x + y

    fun log(msg: String, tag: String) {
        System.out.println("[$tag] $msg")
    }

    // 函数里面加入参数
    private fun testLambda(funcResult: (String) -> String) {
        //val funcResult1: Int = funcResult("hello world")
        val funcResult1 = funcResult("hello world")
        log("result = {$funcResult1+1} ", "lesson_3")
    }


    private fun setCallBack(call: TestInterface) {
        val index = call.index(1)
        log("index=$index", "lesson_3")
    }

    public interface TestInterface {
        fun index(id: Int): String
    }

    @Test
    public fun lesson_05(){
        // 85
        // Kotlin Android Extensions
        // classpath "org.jetbrains.kotlin:kotlin-android-extensions:$kotlin_version"

        // import kotlinx.android.synthetic.activity_main.*
        // import kotlinx.android.synthetic.content_main.*
        // import kotlinx.android.synthetic.view_item.view.*

    }
}
