package com.huangrui.dormitory.net.bean

data class ListWrappers<T>(
    val data: List<T>,
    val total: Int
)