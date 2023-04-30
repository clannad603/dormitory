package com.huangrui.dormitory.repository

import com.huangrui.dormitory.net.bean.Announce
import com.huangrui.dormitory.net.bean.BasicBean
import com.huangrui.dormitory.net.bean.ListWrappers
import com.huangrui.dormitory.net.http.service.AnnounceService
import com.huangrui.dormitory.net.http.service.RoomService
import com.huangrui.dormitory.net.param.AnnounceParam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnnounceRepository @Inject constructor(private var announceService: AnnounceService){

    suspend fun getAnnouncements(announceParam: AnnounceParam): BasicBean<ListWrappers<Announce>> {
        return announceService.getAnnouncements(announceParam)
    }

    suspend fun getAnnouncement(id: String): BasicBean<Announce>{
        return announceService.getAnnouncement(id)
    }

}