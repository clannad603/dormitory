package com.huangrui.dormitory.ui.login.facelogin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.repository.UserInfoRepository
import com.huangrui.dormitory.utils.AppUserUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceLoginViewModel @Inject constructor(
    private var repository: UserInfoRepository
) : ViewModel() {
    var viewStates by mutableStateOf(FaceLoginViewState())
        private set

    private val _viewEvents = Channel<FaceLoginViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    private fun updateAccount(faceBase64: String) {
        viewStates = viewStates.copy(faceBase64 = faceBase64)
    }

    fun dispatch(action: FaceLoginViewAction) {
        when (action) {
            is FaceLoginViewAction.Login -> login()
            is FaceLoginViewAction.UpdateAccount -> updateAccount(action.faceBase64)
            else -> {}
        }
    }

    private fun login() {
        viewModelScope.launch {
            flow {
                emit(repository.faceLogin(viewStates.faceBase64))
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                AppUserUtil.onLogin()
                AppUserUtil.token = it.result
                _viewEvents.send(FaceLoginViewEvent.LoginOK)
            }.catch {
                _viewEvents.send(FaceLoginViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }
}

data class FaceLoginViewState(
    var faceBase64: String = "",
    val isLogin: Boolean = false
)

sealed class FaceLoginViewAction {
    object Login : FaceLoginViewAction()
    data class UpdateAccount(val faceBase64: String) : FaceLoginViewAction()
}
sealed class FaceLoginViewEvent {
    object LoginOK : FaceLoginViewEvent()
    data class ErrorMessage(val message: String) : FaceLoginViewEvent()
}