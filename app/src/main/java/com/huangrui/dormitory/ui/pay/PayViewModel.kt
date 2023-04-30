package com.huangrui.dormitory.ui.pay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.net.param.PayParam
import com.huangrui.dormitory.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PayViewModel @Inject constructor(
    private var roomRepository: RoomRepository,
) : ViewModel() {
    var viewStates by mutableStateOf(PayViewState())
        private set

    private val _viewEvents = Channel<PayViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: PayViewAction){
        when(action){
            is PayViewAction.Pay -> pay()
            is PayViewAction.UpdateCost -> updateCost(action.pay)
            is PayViewAction.InitTypeAndId -> initTypeAndId(action.type,action.id)
        }
    }

    private fun initTypeAndId(type: Int, id: String) {
        viewStates = viewStates.copy(type = type, id = id)
    }

    private fun updateCost(pay: String) {
        viewStates = viewStates.copy(pay =pay )
    }

    private fun pay() {
        viewModelScope.launch {
            flow {
            if (viewStates.type==0){
                emit(roomRepository.payWater(PayParam(viewStates.pay.toDouble(),viewStates.id)))
            }else{
                emit(roomRepository.payPower(PayParam(viewStates.pay.toDouble(),viewStates.id)))
            }
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                _viewEvents.send(PayViewEvent.PayOK)
            }.catch {
                _viewEvents.send(PayViewEvent.ErrorMessage(it.message ?: ""))
            }.collect()
        }
    }
}

data class PayViewState(
    val id: String="",
    val pay:String="",
    val type :Int= 0
)

sealed class PayViewAction{
    object Pay:PayViewAction()
    data class UpdateCost(val pay:String):PayViewAction()
    data class InitTypeAndId(val type:Int,val id :String):PayViewAction()
}

sealed class PayViewEvent {
    object PayOK : PayViewEvent()
    data class ErrorMessage(val message: String) : PayViewEvent()
}

