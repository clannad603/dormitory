package com.huangrui.dormitory.ui.repair.repairContent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.repository.AnnounceRepository
import com.huangrui.dormitory.repository.RepairRepository
import com.huangrui.dormitory.ui.login.login.LoginViewEvent
import com.huangrui.dormitory.ui.systemInfo.content.AnnounceContentViewAction
import com.huangrui.dormitory.ui.systemInfo.content.AnnounceContentViewState
import com.huangrui.dormitory.utils.AppUserUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepairContentViewModel @Inject constructor(
    private var repairRepository: RepairRepository
) : ViewModel()   {
    var viewStates by mutableStateOf(RepairContentViewState())
        private set
    private val _viewEvents = Channel<RepairViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()
    fun dispatch(action: RepairContentViewAction){
        when(action){
            is RepairContentViewAction.GetContent ->getContent()
            is RepairContentViewAction.InitId ->initId(action.id)
            is RepairContentViewAction.DeleteRepair->deleteRepair(action.id)
        }
    }

    private fun deleteRepair(id: String) {
        viewModelScope.launch {
            flow {
                emit(repairRepository.deleteRepairInfo(viewStates.id))
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                _viewEvents.send(RepairViewEvent.DeleteOK)
            }.catch {
                _viewEvents.send(RepairViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }

    }

    private fun getContent() {
        viewModelScope.launch {
            flow {
                emit(repairRepository.getRepairInfo(viewStates.id))
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                viewStates = viewStates.copy(
                    title = it.result.title,
                    content = it.result.content,
                    commitTime = it.result.commitTime,
                    status = it.result.status,
                    type = it.result.type
                )
            }.catch {

            }.collect()
        }
    }

    private fun initId(id: String) {
        viewStates = viewStates.copy(id = id)
    }
}
sealed class RepairContentViewAction{
    object GetContent :RepairContentViewAction()
    data class InitId(val id: String):RepairContentViewAction()
    data class DeleteRepair(val id:String):RepairContentViewAction()
}
data class RepairContentViewState(
    val commitTime: String="",
    val content: String="",
    val id: String="",
    val status: Int=0,
    val title: String="",
    val type: String="",
)
sealed class RepairViewEvent {
    object DeleteOK :RepairViewEvent()
    data class ErrorMessage(val message: String) : RepairViewEvent()
}