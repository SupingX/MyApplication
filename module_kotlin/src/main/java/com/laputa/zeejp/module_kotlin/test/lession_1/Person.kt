package com.laputa.zeejp.module_kotlin.test.lession_1

class Persion(name: String, age: Int = 100, sex: String?) : Any(name) {
   /* fun sayHi(hi: String = "hello girls"): String {
        return "say:\"$hi\""
    }*/

    fun sayHi(hi: String = "hello girls",tag:String = javaClass.simpleName): String {
        return "$tag say:\"$hi\""
    }

}