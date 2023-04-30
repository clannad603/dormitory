package com.huangrui.dormitory.net.bean

data class BasicBean<T>(
    val code: Int,
    val data: T,
    val msg: String,
    val success: Boolean
)