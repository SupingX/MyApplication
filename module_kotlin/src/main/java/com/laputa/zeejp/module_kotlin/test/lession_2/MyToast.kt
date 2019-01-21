package com.laputa.zeejp.module_kotlin.test.lession_2

import android.content.Context
import android.widget.TextView
import android.widget.Toast

// 扩展函数
fun Context.mytoast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun String.myClip(name: String): String = "test 扩展函数 $name"

public var TextView.mytext: CharSequence
    get() = "my:$text"
    set(value) = setText(value)
