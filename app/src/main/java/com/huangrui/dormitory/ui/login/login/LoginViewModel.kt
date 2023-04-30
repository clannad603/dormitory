package com.huangrui.dormitory.ui.login.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.net.param.LoginParam
import com.huangrui.dormitory.repository.UserInfoRepository
import com.huangrui.dormitory.utils.AppUserUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var repository: UserInfoRepository
) : ViewModel() {
    var viewStates by mutableStateOf(LoginViewState())
        private set

    private val _viewEvents = Channel<LoginViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()
    fun dispatch(action: LoginViewAction) {
        when (action) {
            is LoginViewAction.Login -> login()
            is LoginViewAction.ClearAccount -> clearAccount()
            is LoginViewAction.ClearPassword -> clearPassword()
            is LoginViewAction.UpdateAccount -> updateAccount(action.username)
            is LoginViewAction.UpdatePassword -> updatePassword(action.password)
        }
    }

    private fun login() {
        viewModelScope.launch {
            flow {
                emit(repository.login(LoginParam(viewStates.username,viewStates.password)))
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                AppUserUtil.onLogin()
                AppUserUtil.token = it.result
                _viewEvents.send(LoginViewEvent.LoginOK)
            }.catch {
                _viewEvents.send(LoginViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }

    private fun clearAccount() {
        viewStates = viewStates.copy(username = "")
    }

    private fun clearPassword() {
        viewStates = viewStates.copy(password = "")
    }

    private fun updateAccount(account: String) {
        viewStates = viewStates.copy(username = account)
    }

    private fun updatePassword(password: String) {
        viewStates = viewStates.copy(password = password)
    }
}

data class LoginViewState(
    val username: String = "",
    val password: String = "",
    val isLogin: Boolean = false
)

sealed class LoginViewAction {
    object Login : LoginViewAction()
    object ClearAccount : LoginViewAction()
    object ClearPassword : LoginViewAction()
    data class UpdateAccount(val username: String) : LoginViewAction()
    data class UpdatePassword(val password: String) : LoginViewAction()
}

sealed class LoginViewEvent {
    object LoginOK : LoginViewEvent()
    data class ErrorMessage(val message: String) : LoginViewEvent()
}
