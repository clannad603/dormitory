package com.huangrui.dormitory.ui.systemInfo.content

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.repository.AnnounceRepository
import com.huangrui.dormitory.ui.login.register.RegisterViewEvent
import com.huangrui.dormitory.ui.login.register.RegisterViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnounceContentViewModel @Inject constructor(
    private var announceRepository: AnnounceRepository,
) : ViewModel()   {
    var viewStates by mutableStateOf(AnnounceContentViewState())
        private set

    fun dispatch(action: AnnounceContentViewAction){
        when(action){
            is AnnounceContentViewAction.GetContent ->getContent()
            is AnnounceContentViewAction.InitId ->initId(action.id)
        }
    }

    private fun getContent() {
        viewModelScope.launch {
            flow {
                emit(announceRepository.getAnnouncement(viewStates.id))
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
               viewStates = viewStates.copy(title = it.result.title, content = it.result.content, publisher = it.result.publisher, publishTime =it.result.publishTime)
            }.catch {

            }.collect()
        }
    }

    private fun initId(id: String) {
            viewStates = viewStates.copy(id = id)
    }
}

sealed class AnnounceContentViewAction{
    object GetContent :AnnounceContentViewAction()
    data class InitId(val id: String):AnnounceContentViewAction()
}

data class AnnounceContentViewState(
    val id: String="",
    val title: String="",
    val content:String ="",
    val publisher:String = "",
    val publishTime:String = "",
)