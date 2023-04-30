package com.huangrui.dormitory.ui.login.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.bean.Major
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.net.param.RegisterParam
import com.huangrui.dormitory.repository.UserInfoRepository
import com.huangrui.dormitory.utils.AppUserUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private var repository: UserInfoRepository
) : ViewModel() {
    var viewStates by mutableStateOf(RegisterViewState())
        private set

    private val _viewEvents = Channel<RegisterViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            flow {
                emit(repository.getMajors())
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                viewStates.major = it.result
            }.catch {
                _viewEvents.send(RegisterViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }
    fun dispatch(viewAction: RegisterViewAction){
        when(viewAction){
            is RegisterViewAction.DoRegister->register()
            is RegisterViewAction.UpdateID-> updateId(viewAction.id)
            is RegisterViewAction.UpdateFaceBase64 ->updateFaceBase64(viewAction.faceBase64)
            is RegisterViewAction.UpdateMajor -> updateMajor(viewAction.majorId)
            is RegisterViewAction.UpdateName -> updateName(viewAction.name)
            is RegisterViewAction.UpdatePassword -> updatePassword(viewAction.password)
            is RegisterViewAction.ConfirmPassword -> updateConfirmPassword(viewAction.confirmPassword)
        }
    }
    private fun updateConfirmPassword(confirmPassword: String){
        viewStates = viewStates.copy(confirmPassword = confirmPassword)
    }
    private fun updateName(name: String){
        viewStates = viewStates.copy(name = name)
    }

    private fun updatePassword(password: String){
        viewStates = viewStates.copy(password = password)
    }

    private fun updateMajor(majorId: Int){
        viewStates = viewStates.copy(majorId = majorId)
    }

    private fun register(){
        if (viewStates.password==viewStates.confirmPassword){
            viewModelScope.launch {
                flow {
                    emit(repository.register(RegisterParam(viewStates.faceBase64,viewStates.id,viewStates.majorId,viewStates.password)))
                }.map {
                    if (it.success){
                        HttpResult.Success(it.data)
                    }else{
                        throw Exception(it.msg)
                    }
                }.onEach {
                    AppUserUtil.onLogin()
                    AppUserUtil.token = it.result
                    _viewEvents.send(RegisterViewEvent.RegisterOK)
                }.catch {
                    _viewEvents.send(RegisterViewEvent.ErrorMessage(it.message ?: ""))
                }.collect()
            }
        }else{
            viewModelScope.launch {
                _viewEvents.send(RegisterViewEvent.ErrorMessage("密码不一致"))
            }
        }

    }

    private fun updateId(id: String){
        viewStates = viewStates.copy(id = id)
    }

    private fun updateFaceBase64(faceBase64: String){
        viewStates = viewStates.copy(faceBase64 = faceBase64)
    }
}

data class RegisterViewState(
    val faceBase64: String = "",
    var major:MutableList<Major> = mutableListOf(),
    val id :String = "",
    val majorId:Int = 0,
    val name:String = "",
    val confirmPassword :String= "",
    val password:String = "",
    val isLogin: Boolean = false
)

sealed class RegisterViewAction{
    object DoRegister :  RegisterViewAction()
    data class UpdateName(val name: String) : RegisterViewAction()
    data class UpdateMajor(val majorId: Int) : RegisterViewAction()
    data class UpdateID(val id:String):RegisterViewAction()
    data class UpdateFaceBase64(val faceBase64: String):RegisterViewAction()
    data class UpdatePassword(val password: String) : RegisterViewAction()
    data class ConfirmPassword(val confirmPassword: String) : RegisterViewAction()
}
sealed class  RegisterViewEvent {
    object RegisterOK : RegisterViewEvent()
    data class ErrorMessage(val message: String) :RegisterViewEvent()
}