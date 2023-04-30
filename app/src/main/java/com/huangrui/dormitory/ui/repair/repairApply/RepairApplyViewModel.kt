package com.huangrui.dormitory.ui.repair.repairApply

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.bean.Major
import com.huangrui.dormitory.net.bean.RepairType
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.net.param.RepairContentParam
import com.huangrui.dormitory.repository.RepairRepository
import com.huangrui.dormitory.ui.login.register.RegisterViewEvent
import com.huangrui.dormitory.ui.login.register.RegisterViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepairApplyViewModel @Inject constructor(
    private var repairRepository: RepairRepository,
) : ViewModel()  {
    var viewStates by mutableStateOf(RepairApplyViewState())
        private set

    private val _viewEvents = Channel<RepairApplyViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: RepairApplyViewAction){
        when(action){
            is RepairApplyViewAction.RepairApply -> repairApply()
            is RepairApplyViewAction.UpdateType-> updateType(action.type)
            is RepairApplyViewAction.UpdateContent->updateContent(action.content)
            is RepairApplyViewAction.UpdateFace-> updateFace(action.face)
            is RepairApplyViewAction.UpdateTitle-> updateTitle(action.title)
        }
    }

    private fun updateTitle(title: String) {
        viewStates = viewStates.copy(title=title)
    }

    private fun updateFace(face: String) {
        viewStates = viewStates.copy(face=face)
    }

    private fun updateContent(content: String) {
        viewStates = viewStates.copy(content=content)
    }

    private fun updateType(type: Int) {
       viewStates = viewStates.copy(type = type)
    }

    private fun repairApply() {
        viewModelScope.launch {
            flow {
                emit(repairRepository.addNewRepair(
                    RepairContentParam(
                        viewStates.content,
                        viewStates.face,
                        viewStates.title,
                        viewStates.type)
                )
                )
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                _viewEvents.send(RepairApplyViewEvent.RepairApplyOK)
            }.catch {
                _viewEvents.send(RepairApplyViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }

    init {
        viewModelScope.launch {
            flow {
                emit(repairRepository.getRepairTypeList())
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                viewStates.types = it.result

            }.catch {
            }.collect()
        }
    }
}

data class RepairApplyViewState(
    val content: String="",
    val face:String="",
    val title:String="",
    val type:Int=0,
    var types:List<RepairType> = mutableListOf(),
)

sealed class RepairApplyViewAction{
    object RepairApply: RepairApplyViewAction()
    data class UpdateFace(val face:String):RepairApplyViewAction()
    data class UpdateContent(val content:String):RepairApplyViewAction()
    data class UpdateTitle(val title: String):RepairApplyViewAction()
    data class UpdateType(val type:Int):RepairApplyViewAction()
}
sealed class  RepairApplyViewEvent {
    object RepairApplyOK : RepairApplyViewEvent()
    data class ErrorMessage(val message: String) :RepairApplyViewEvent()
}