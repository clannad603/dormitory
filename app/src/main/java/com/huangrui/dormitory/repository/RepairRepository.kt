package com.huangrui.dormitory.repository

import com.huangrui.dormitory.net.bean.BasicBean
import com.huangrui.dormitory.net.bean.ListWrappers
import com.huangrui.dormitory.net.bean.RepairInfo
import com.huangrui.dormitory.net.bean.RepairType
import com.huangrui.dormitory.net.http.service.RepairService
import com.huangrui.dormitory.net.http.service.RoomService
import com.huangrui.dormitory.net.param.RepairContentParam
import com.huangrui.dormitory.net.param.RepairParam
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepairRepository @Inject constructor(private var repairService: RepairService) {

    suspend fun getRepairInfoList(params: RepairParam): BasicBean<ListWrappers<RepairInfo>> {
        return repairService.getRepairInfoList(params)
    }

    suspend fun getRepairTypeList(): BasicBean<List<RepairType>> {
        return repairService.getRepairTypeList()
    }

    suspend fun getRepairInfo(id: String): BasicBean<RepairInfo> {
        return repairService.getRepairInfo(id)
    }

    suspend fun deleteRepairInfo(id: String): BasicBean<String>{
        return repairService.deleteRepairInfo(id)
    }

    suspend fun addNewRepair(params: RepairContentParam): BasicBean<String> {
        return repairService.addNewRepair(params)
    }

}