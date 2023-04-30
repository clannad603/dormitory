package com.huangrui.dormitory.net.bean

data class UserInfo(
    val building: String,
    val id: Int,
    val major: String,
    val name: String,
    val room: String,
    val score: Int,
    val sex: Int
)