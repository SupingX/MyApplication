package com.laputa.zeejp.module_kotlin.test.lession_2

class Book(author: String) {
    var name: String = ""
        get() = field.toUpperCase()
        set(value) {
            field = "Name $value"
        }
    var author: Int = 0
        get() = field++
}