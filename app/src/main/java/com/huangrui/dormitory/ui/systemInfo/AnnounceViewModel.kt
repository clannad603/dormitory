package com.huangrui.dormitory.ui.systemInfo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.huangrui.dormitory.common.paging.simplePager
import com.huangrui.dormitory.net.bean.Announce
import com.huangrui.dormitory.net.param.AnnounceParam
import com.huangrui.dormitory.repository.AnnounceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnnounceViewModel  @Inject constructor(
    private var announceRepository: AnnounceRepository,
) : ViewModel()  {
    private val pager by lazy {
            simplePager {
                    page, pageSize -> announceRepository.getAnnouncements(AnnounceParam(page,pageSize))
            }.cachedIn(viewModelScope)
    }
    var viewStates by mutableStateOf(AnnounceViewState())
        private set

    fun dispatch(action: AnnounceViewAction) {
        when (action) {
            is AnnounceViewAction.OnStart -> onStart()
            is AnnounceViewAction.Refresh -> refresh()
        }
    }

    private fun refresh() {

    }

    private fun onStart() {
        viewStates = viewStates.copy(pagingData = pager)
    }
}


data class AnnounceViewState(
    val isRefreshing: Boolean = false,
    val pagingData: PagingAnnounce?=null,
)

sealed class AnnounceViewAction {
    object OnStart : AnnounceViewAction()
    object Refresh : AnnounceViewAction()
}
typealias PagingAnnounce = Flow<PagingData<Announce>>