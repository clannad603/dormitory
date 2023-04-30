package com.huangrui.dormitory.net.bean

data class RoomInfo(
    val base: String,
    val building: String,
    val id: Long,
    val power: Double,
    val room: String,
    val score: Int,
    val water: Double
)