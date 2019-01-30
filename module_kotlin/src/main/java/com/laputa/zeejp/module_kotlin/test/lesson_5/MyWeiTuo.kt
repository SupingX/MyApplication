package com.laputa.zeejp.module_kotlin.test.lesson_5

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 自定义委托
 * 此委托可以作用在任何非null的类型 接受任何类型的引用，然后像getter/setter那样使用T
 */
class MyWeiTuo<T> : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw  IllegalStateException("${property.name} not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }



}

