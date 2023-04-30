package com.huangrui.dormitory.repository

import com.huangrui.dormitory.net.bean.*
import com.huangrui.dormitory.net.http.service.RoomService
import com.huangrui.dormitory.net.param.PayParam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(private var roomService: RoomService){

    suspend fun getRoomInfo(): BasicBean<RoomInfo> {
        return roomService.getRoomInfo()
    }

    suspend fun getBuildingInfo(): BasicBean<MutableList<Building>> {
        return roomService.getBuildingInfo()
    }

    suspend fun getRoomPower(): BasicBean<Power>{
        return roomService.getRoomPower()
    }

    suspend fun getRoomWater(): BasicBean<Water> {
        return roomService.getRoomWater()
    }

    suspend fun payWater(payParam: PayParam): BasicBean<PayBack> {
        return roomService.payWater(payParam)
    }

    suspend fun payPower(payParam: PayParam): BasicBean<PayBack> {
        return roomService.payPower(payParam)
    }
}