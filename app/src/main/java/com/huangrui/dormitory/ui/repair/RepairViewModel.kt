package com.huangrui.dormitory.ui.repair

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.huangrui.dormitory.common.paging.simplePager
import com.huangrui.dormitory.net.bean.Announce
import com.huangrui.dormitory.net.bean.RepairInfo
import com.huangrui.dormitory.net.param.AnnounceParam
import com.huangrui.dormitory.net.param.RepairParam
import com.huangrui.dormitory.repository.RepairRepository
import com.huangrui.dormitory.repository.RoomRepository
import com.huangrui.dormitory.ui.systemInfo.AnnounceViewAction
import com.huangrui.dormitory.ui.systemInfo.AnnounceViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RepairViewModel  @Inject constructor(
    private var repairRepository: RepairRepository,
) : ViewModel()  {
    private val pager by lazy {
        simplePager {
                page, pageSize -> repairRepository.getRepairInfoList(RepairParam(page,pageSize))
        }.cachedIn(viewModelScope)
    }

    var viewStates by mutableStateOf(RepairViewState())
        private set

    fun dispatch(action: RepairViewAction) {
        when (action) {
            is RepairViewAction.OnStart -> onStart()
            is RepairViewAction.Refresh -> refresh()
        }
    }

    private fun refresh() {

    }

    private fun onStart() {
        viewStates = viewStates.copy(pagingData = pager)
    }

}
data class RepairViewState(
    val pagingData: PagingRepair?=null,
)
sealed class RepairViewAction {
    object OnStart : RepairViewAction()
    object Refresh : RepairViewAction()
}
typealias PagingRepair = Flow<PagingData<RepairInfo>>