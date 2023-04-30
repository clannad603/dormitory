package com.huangrui.dormitory.net.bean

data class RepairInfo(
    val commitTime: String,
    val content: String,
    val id: String,
    val status: Int,
    val title: String,
    val type: String,
    val userId: Int
)