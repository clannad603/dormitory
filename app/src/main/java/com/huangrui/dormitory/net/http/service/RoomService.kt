package com.huangrui.dormitory.net.http.service

import com.huangrui.dormitory.net.bean.*
import com.huangrui.dormitory.net.param.PayParam
import com.huangrui.dormitory.utils.AppUserUtil
import com.huangrui.dormitory.utils.AppUserUtil.token
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT

interface RoomService {


    @GET("/dor/info")
//    @Headers("Authorization: eyJhbGciOiJIUzI1NiJ9.eyJsb2dpblVzZXJuYW1lIjoiMjAyMDIxMTM3MiIsImV4cCI6MTY4MDg3MDU2OCwiaWF0IjoxNjgwMjY1NzY4fQ.Ge3w-zJSBDdsFMgXgNWfRvF9ZSo2cW-72a0ihxCvC_E")
    suspend fun getRoomInfo():BasicBean<RoomInfo>

    @GET("/dor/info/build")
//    @Headers("Authorization: eyJhbGciOiJIUzI1NiJ9.eyJsb2dpblVzZXJuYW1lIjoiMjAyMDIxMTM3MiIsImV4cCI6MTY4MDg3MDU2OCwiaWF0IjoxNjgwMjY1NzY4fQ.Ge3w-zJSBDdsFMgXgNWfRvF9ZSo2cW-72a0ihxCvC_E")
    suspend fun getBuildingInfo():BasicBean<MutableList<Building>>

    @GET("/dor/info/power")
    suspend fun getRoomPower():BasicBean<Power>

    @GET("/dor/info/water")
    suspend fun getRoomWater():BasicBean<Water>

    @PUT("/dor/info/power")
    suspend fun payPower(@Body payParam: PayParam):BasicBean<PayBack>

    @PUT("/dor/info/water")
    suspend fun payWater(@Body payParam: PayParam):BasicBean<PayBack>
}