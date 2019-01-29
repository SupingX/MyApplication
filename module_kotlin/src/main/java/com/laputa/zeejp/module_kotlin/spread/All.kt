package com.laputa.zeejp.module_kotlin.spread

import android.view.View
import android.view.ViewGroup

operator fun ViewGroup.get(position:Int) : View = getChildAt(position)