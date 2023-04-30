package com.huangrui.dormitory.net.http.service

import com.huangrui.dormitory.net.bean.BasicBean
import com.huangrui.dormitory.net.bean.CardInfo
import com.huangrui.dormitory.net.bean.Major
import com.huangrui.dormitory.net.bean.UserInfo
import com.huangrui.dormitory.net.param.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface UserService {

    @POST("/dor/user/login")
    suspend fun login(@Body params:LoginParam):BasicBean<String>

    @GET("/dor/user/info")
    suspend fun getUserInfo():BasicBean<UserInfo>

    @POST("/dor/user/face")
    suspend fun faceLogin(@Body params: FaceLoginParam):BasicBean<String>

    @POST("/dor/user/registry")
    suspend fun register(@Body params: RegisterParam):BasicBean<String>

    @GET("/dor/user/major")
    suspend fun getMajors():BasicBean<MutableList<Major>>

    @POST("/dor/user")
    suspend fun bindRoom(@Body params: RoomParam):BasicBean<String>

    @GET("/dor/user/logout")
    suspend fun logout():BasicBean<String>

    @POST("/dor/sug")
    suspend fun giveSug(@Body params:SugParam):BasicBean<String>

    @POST("/dor/user/card")
    suspend fun giveCard(@Body params: CardParam):BasicBean<CardInfo>

    @GET("/dor/user/card")
    suspend fun getCard():BasicBean<CardInfo>
}