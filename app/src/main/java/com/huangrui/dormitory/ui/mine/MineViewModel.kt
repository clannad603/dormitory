package com.huangrui.dormitory.ui.mine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.net.param.CardParam
import com.huangrui.dormitory.net.param.SugParam
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
class MineViewModel @Inject constructor(
    private var repository: UserInfoRepository
) : ViewModel() {
    var viewStates by mutableStateOf(MineViewState())
        private set

    private val _viewEvents = Channel<MineViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: MineViewAction){
        when(action){
            is MineViewAction.Logout ->logout()
            is MineViewAction.StartLocation->startLocation()
            is MineViewAction.GiveCard -> giveCard()
            is MineViewAction.GiveSug -> giveSug()
            is MineViewAction.GetStatus -> getStatus()
            is MineViewAction.UpdateAddress -> updateAddress(action.address)
            is MineViewAction.UpdateFace ->updateFace(action.face)
            is MineViewAction.UpdateSug -> updateSug(action.sug)
        }
    }

    private fun startLocation() {
        viewModelScope.launch {
            _viewEvents.send(MineViewEvent.StartLocation)
        }
    }

    private fun updateSug(sug: String) {
        viewStates = viewStates.copy(sug = sug)
    }

    private fun updateFace(face: String) {
        viewStates = viewStates.copy(face = face)
    }

    private fun updateAddress(address: String) {
        viewStates = viewStates.copy(address=address)
    }

    private fun getStatus() {
        viewModelScope.launch {
            flow {
                emit(repository.getCard())
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                viewStates = viewStates.copy(type = it.result.typeId)
            }.catch {

            }.collect()
        }
    }

    private fun giveSug() {
        viewModelScope.launch {
            flow {
                emit(repository.giveSvg(SugParam(viewStates.sug)))
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {

            }.catch {
                _viewEvents.send(MineViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }

    private fun giveCard() {
        viewModelScope.launch {
            flow {
                emit(repository.giveCard(CardParam(viewStates.address,viewStates.face)))
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                viewStates = viewStates.copy(type = it.result.typeId)
                _viewEvents.send(MineViewEvent.CardOk)
            }.catch {
                _viewEvents.send(MineViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            flow {
                emit(repository.logOut())
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                AppUserUtil.onLogOut()
                AppUserUtil.token = ""
                _viewEvents.send(MineViewEvent.LogoutOK)
            }.catch {
                _viewEvents.send(MineViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }
}
data class MineViewState(
    val address: String="",
    val face: String="",
    val type:Int=0,
    val sug:String="",
    val cardOk:Boolean=false
)

sealed class MineViewAction{
    object Logout :MineViewAction()
    object GiveCard:MineViewAction()
    object GiveSug:MineViewAction()
    object GetStatus:MineViewAction()
    object StartLocation:MineViewAction()
    data class UpdateAddress(val address:String):MineViewAction()
    data class UpdateFace(val face:String):MineViewAction()
    data class UpdateSug(val sug:String):MineViewAction()
}


sealed class MineViewEvent {
    object LogoutOK : MineViewEvent()
    object CardOk:MineViewEvent()
    object StartLocation:MineViewEvent()
    data class ErrorMessage(val message: String) : MineViewEvent()
}