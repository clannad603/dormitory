package com.huangrui.dormitory.net.http.service

import com.huangrui.dormitory.net.bean.*
import com.huangrui.dormitory.net.param.AnnounceParam
import com.huangrui.dormitory.net.param.RepairContentParam
import com.huangrui.dormitory.net.param.RepairParam
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RepairService {

    @POST("/dor/work/info")
    suspend fun getRepairInfoList(@Body param: RepairParam):BasicBean<ListWrappers<RepairInfo>>

    @POST("/dor/work")
    suspend fun addNewRepair(@Body param:RepairContentParam):BasicBean<String>

    @GET("/dor/work/type")
    suspend fun getRepairTypeList():BasicBean<List<RepairType>>

    @GET("/dor/work/{id}")
    suspend fun getRepairInfo(@Path("id") id: String):BasicBean<RepairInfo>

    @DELETE("/dor/work/{id}")
    suspend fun deleteRepairInfo(@Path("id") id: String):BasicBean<String>
}