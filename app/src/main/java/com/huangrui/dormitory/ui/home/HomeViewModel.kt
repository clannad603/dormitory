package com.huangrui.dormitory.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huangrui.dormitory.R
import com.huangrui.dormitory.net.http.HttpResult
import com.huangrui.dormitory.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var roomRepository: RoomRepository,
) : ViewModel() {
    var viewStates by mutableStateOf(HomeViewState())
        private set

    init {
        viewStates = viewStates.copy(
            images = listOf(
                R.drawable.banner,
                R.drawable.banner,
                R.drawable.banner
            )
        )
        getUserInfo()
        getPowerCost()
        getWaterCost()
    }

    private fun getWaterCost(){
        viewModelScope.launch {
            flow {
                emit(roomRepository.getRoomWater())
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                viewStates = viewStates.copy(waterAmount = it.result.amount, waterOverDue = it.result.overdue, waterId = it.result.id)
            }.catch {

            }.collect()
        }
    }

    private fun getPowerCost(){
        viewModelScope.launch {
            flow {
                emit(roomRepository.getRoomPower())
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                viewStates = viewStates.copy(powerAmount = it.result.amount,powerOverDue = it.result.overdue, powerId = it.result.id)
            }.catch {

            }.collect()
        }
    }

    fun dispatch(viewAction: HomeViewAction){
        when(viewAction){
            HomeViewAction.Init ->getUserInfo()
        }
    }

    private fun getUserInfo(){
        viewModelScope.launch {
            flow {
                emit(roomRepository.getRoomInfo())
            }.map {
                if (it.success){
                    HttpResult.Success(it.data)
                }else{
                    throw Exception(it.msg)
                }
            }.onEach {
                if (it.result.building!=null){
                    viewStates = viewStates.copy(building = it.result.building)
                }
                if (it.result.room!=null){
                    viewStates = viewStates.copy(room = it.result.room)
                }
                if (it.result.water!=null){
                    viewStates = viewStates.copy(water =it.result.water)
                }
                if (it.result.power!=null){
                    viewStates = viewStates.copy(power =it.result.power)
                }
                if (it.result.score!=null){
                    viewStates = viewStates.copy(score =it.result.score)
                }
            }.catch {

            }.collect()
        }
    }
}

data class HomeViewState(
    val images: List<Int> = emptyList(),
    val building:String = "",
    val room :String = "",
    val score:Int = 0,
    val water:Double = 0.0,
    val waterAmount:Int = 0,
    val waterOverDue:Boolean=false,
    val waterId:String = "",
    val power:Double = 0.0,
    val powerAmount:Int = 0,
    val powerOverDue:Boolean=false,
    val powerId:String = "",
)

sealed class HomeViewAction{
    object Init:HomeViewAction()
}