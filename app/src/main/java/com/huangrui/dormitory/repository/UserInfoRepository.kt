package com.huangrui.dormitory.repository

import com.huangrui.dormitory.net.bean.BasicBean
import com.huangrui.dormitory.net.bean.CardInfo
import com.huangrui.dormitory.net.bean.Major
import com.huangrui.dormitory.net.http.service.UserService
import com.huangrui.dormitory.net.param.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepository @Inject constructor(private var userService: UserService){

    suspend fun login(params: LoginParam): BasicBean<String> {
        return userService.login(params)
    }


    suspend fun faceLogin(faceBase64: String): BasicBean<String>{
        return userService.faceLogin(FaceLoginParam(faceBase64))
    }

    suspend fun getMajors(): BasicBean<MutableList<Major>> {
        return userService.getMajors()
    }

    suspend fun register(params:RegisterParam): BasicBean<String> {
        return userService.register(params)
    }

    suspend fun bindRoom(params: RoomParam): BasicBean<String>{
        return userService.bindRoom(params)
    }

    suspend fun logOut(): BasicBean<String> {
        return userService.logout()
    }

    suspend fun giveSvg(param: SugParam): BasicBean<String> {
        return userService.giveSug(param)
    }

    suspend fun getCard(): BasicBean<CardInfo> {
        return userService.getCard()
    }

    suspend fun giveCard(param: CardParam): BasicBean<CardInfo> {
        return userService.giveCard(param)
    }
}