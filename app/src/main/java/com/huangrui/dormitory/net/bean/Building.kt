package com.huangrui.dormitory.net.bean

data class Building(
    val id: String,
    val name: String,
    val rooms: List<Room>
)