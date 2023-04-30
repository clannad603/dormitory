package com.huangrui.dormitory.ui.bindRoom

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.huangrui.dormitory.DormitoryApplication
import com.huangrui.dormitory.R
import com.huangrui.dormitory.ui.common.LoginRoute
import com.huangrui.dormitory.ui.common.RouteName
import com.huangrui.dormitory.ui.login.login.LoginViewAction
import com.huangrui.dormitory.ui.login.login.LoginViewEvent
import com.huangrui.dormitory.ui.login.register.RegisterViewAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BindRoomPage(
    navCtrl: NavHostController,
    from:String,
    viewModel: BindViewModel = hiltViewModel()
) {
    val viewStates = viewModel.viewStates
    var isBuildClick by remember{ mutableStateOf(false) }
    var selectBuild by remember{ mutableStateOf("请选择楼栋")}
    var isRoomClick by remember{ mutableStateOf(false) }
    var selectRoom by remember {
        mutableStateOf("请选择寝室")
    }
    var clickAble by remember{ mutableStateOf(false)}
    var selectedTag by remember{ mutableStateOf(0) }
    LaunchedEffect(Unit){
        viewModel.viewEvents.collect{
            if (it is BindViewEvent.BindOK){
                if (from=="home"){
                    navCtrl.navigate(RouteName.HOME)
                }else{
                    navCtrl.navigate(LoginRoute.APP_SCAFFOLD)
                }
            }else if (it is BindViewEvent.ErrorMessage){
                Toast.makeText(DormitoryApplication.CONTEXT,it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if(from =="home"){
            TopAppBar(
                modifier = Modifier.height(54.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = {
                        navCtrl.navigateUp()
                    }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                title = {
                    Text("请绑定寝室号", fontSize = 22.sp
                        ,modifier = Modifier
                            .fillMaxSize(0.8f).wrapContentSize(Alignment.Center))
                },
            )
        }else{
            TopAppBar(
                modifier = Modifier.height(54.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = {
                    Text("请绑定寝室号", fontSize = 22.sp
                        ,modifier = Modifier
                            .fillMaxSize(0.8f).wrapContentSize(Alignment.Center))
                },
            )
        }

        Card (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(15.dp),
        ){
            Column (modifier = Modifier.fillMaxSize()){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription ="",
                    alignment = Alignment.Center,
                )
                OutlinedButton(
                    onClick = { isBuildClick = !isBuildClick},
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = selectBuild ,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
                DropdownMenu(
                    expanded = isBuildClick,
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = {},
                    content = {
                        viewStates.buildingInfo.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(text = it.name)
                                }, onClick = {
                                    isBuildClick = !isBuildClick
                                    clickAble = !clickAble
                                    selectBuild = it.name
                                    viewModel.dispatch(BindViewAction.UpdateBuildingId(it.id))
                                    viewModel.dispatch(BindViewAction.UpdateRooms(it.rooms))
                                }
                            )
                        }
                    },
                )
                Spacer(modifier = Modifier.padding(15.dp))
                OutlinedButton(
                    onClick = { isRoomClick = !isRoomClick},
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = selectRoom ,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
                DropdownMenu(
                    expanded = isRoomClick&&clickAble,
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = {},
                    content = {
                        viewStates.roomInfo.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(text = it.name)
                                }, onClick = {
                                    isRoomClick = !isRoomClick
                                    selectRoom = it.name
                                    viewModel.dispatch(BindViewAction.UpdateRoomId(it.id))
                                }
                            )
                        }
                    },
                )
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedTag==0, onClick = {
                        viewModel.dispatch(BindViewAction.UpdateSex(0))
                        selectedTag =0
                    })
                    Text(text = "男", modifier = Modifier.clickable {
                        viewModel.dispatch(BindViewAction.UpdateSex(0))
                        selectedTag = 0
                    }, textAlign = TextAlign.Center)
                    RadioButton(selected = selectedTag==1, onClick = {
                        viewModel.dispatch(BindViewAction.UpdateSex(1))
                        selectedTag =1
                    })
                    Text(text = "女", modifier = Modifier.clickable {
                        viewModel.dispatch(BindViewAction.UpdateSex(1))
                        selectedTag = 1
                    }, textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.padding(15.dp))
                
                Button(
                    onClick = {
                        viewModel.dispatch(BindViewAction.Bind)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "确认绑定", color = Color.White)
                }
            }
        }
    }

}

@Preview
@Composable
fun Test(){
    var selectedTag by remember{ mutableStateOf(0) }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selectedTag==0, onClick = {
            selectedTag =0
        })
        Text(text = "男", modifier = Modifier.clickable {
            selectedTag = 0
        }, textAlign = TextAlign.Center)
        RadioButton(selected = selectedTag==1, onClick = {
            selectedTag =1
        })
        Text(text = "女", modifier = Modifier.clickable {
            selectedTag = 1
        }, textAlign = TextAlign.Center)
    }
}