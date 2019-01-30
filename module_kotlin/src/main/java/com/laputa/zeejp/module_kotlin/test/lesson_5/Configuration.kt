package com.laputa.zeejp.module_kotlin.test.lesson_5

class Configuration(map: Map<String, Any?>) {
    val width: Int by map
    val height: Int by map
    val dp: Int by map
    val deviceName: String by map
}