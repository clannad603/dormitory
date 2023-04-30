package com.huangrui.dormitory.utils

import android.content.Context

fun Context.dp2px(dp:Float):Float{
    val density = resources.displayMetrics.density
    return dp*density+0.5f
}
