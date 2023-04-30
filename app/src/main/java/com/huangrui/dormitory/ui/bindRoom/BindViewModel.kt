package com.huangrui.dormitory.ui.bindRoom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.bean.Building
import com.huangrui.dormitory.net.bean.Room
import com.huangrui.dormitory.net.bean.RoomInfo
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.net.param.RoomParam
import com.huangrui.dormitory.repository.RoomRepository
import com.huangrui.dormitory.repository.UserInfoRepository
import com.huangrui.dormitory.ui.login.login.LoginViewEvent
import com.huangrui.dormitory.ui.login.login.LoginViewState
import com.huangrui.dormitory.utils.AppUserUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BindViewModel @Inject constructor(
    private var userInfoRepository: UserInfoRepository,
    private var roomRepository: RoomRepository
) : ViewModel() {

    var viewStates by mutableStateOf(BindViewState())
        private set

    private val _viewEvents = Channel<BindViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: BindViewAction){
        when(action){
            is BindViewAction.Bind -> bind()
            is BindViewAction.UpdateBuildingId -> updateBuildingId(action.buildingId)
            is BindViewAction.UpdateRoomId -> updateRoomId(action.roomId)
            is BindViewAction.UpdateSex -> updateSex(action.sex)
            is BindViewAction.UpdateRooms -> updateRooms(action.rooms)
        }
    }

    private fun updateRooms(rooms: List<Room>) {
        viewStates = viewStates.copy(roomInfo = rooms)
    }

    private fun updateSex(sex: Int) {
        viewStates = viewStates.copy(sex =  sex)
    }

    private fun updateRoomId(roomId: String) {
        viewStates = viewStates.copy(roomId = roomId)
    }

    private fun updateBuildingId(buildingId: String) {
        viewStates = viewStates.copy(buildingId = buildingId)
    }

    init {
        viewModelScope.launch {
            flow {
                emit(roomRepository.getBuildingInfo())
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                viewStates = viewStates.copy(buildingInfo = it.result)
            }.catch {
                _viewEvents.send(BindViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }
    fun bind(){
        viewModelScope.launch {
            flow {
                emit(userInfoRepository.bindRoom(RoomParam(viewStates.buildingId,viewStates.roomId,viewStates.sex)))
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                _viewEvents.send(BindViewEvent.BindOK)
            }.catch {
                _viewEvents.send(BindViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()

        }
    }
}
data class BindViewState(
    val buildingId: String = "",
    val roomId: String = "",
    val sex:Int = 0,
    val buildingInfo:MutableList<Building> = mutableListOf(),
    val roomInfo:List<Room> = mutableListOf()
)

sealed class BindViewAction{
    object Bind:BindViewAction()
    data class UpdateBuildingId(val buildingId:String):BindViewAction()
    data class UpdateRoomId(val roomId: String):BindViewAction()
    data class UpdateSex(val sex: Int):BindViewAction()
    data class UpdateRooms(val rooms:List<Room>):BindViewAction()
}

sealed class BindViewEvent {
    object BindOK : BindViewEvent()
    data class ErrorMessage(val message: String) : BindViewEvent()
}
