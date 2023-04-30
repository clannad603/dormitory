package com.huangrui.dormitory.net.http.service

import com.huangrui.dormitory.net.bean.Announce
import com.huangrui.dormitory.net.bean.BasicBean
import com.huangrui.dormitory.net.bean.ListWrappers
import com.huangrui.dormitory.net.param.AnnounceParam
import retrofit2.http.*

interface AnnounceService {

    @POST("/dor/announce")
    suspend fun getAnnouncements(@Body announceParam: AnnounceParam):BasicBean<ListWrappers<Announce>>


    @GET("/dor/announce/{id}")
    suspend fun getAnnouncement(@Path("id") id: String):BasicBean<Announce>
}