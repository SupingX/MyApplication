package com.laputa.zeejp.module_kotlin.test.lesson_5

import kotlin.properties.ReadWriteProperty

class  TestWeiTuo{
    var p : String by delegate()

    private fun delegate(): ReadWriteProperty<TestWeiTuo, String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}