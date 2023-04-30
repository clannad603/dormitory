package com.huangrui.dormitory.ui.mine

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.baidu.idl.face.platform.FaceSDKManager
import com.baidu.idl.face.platform.utils.DensityUtils
import com.baidu.location.BDLocation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.huangrui.dormitory.DormitoryApplication
import com.huangrui.dormitory.R
import com.huangrui.dormitory.initial.LocationInstance
import com.huangrui.dormitory.ui.common.LoginRoute
import com.huangrui.dormitory.ui.common.RouteName
import com.huangrui.dormitory.ui.face.FaceLivenessExpActivity
import com.huangrui.dormitory.ui.login.facelogin.FaceLoginViewAction
import com.huangrui.dormitory.ui.login.login.LoginViewEvent
import com.huangrui.dormitory.ui.wigets.TextTabBar
import com.huangrui.dormitory.utils.RouteUtils
import com.huangrui.dormitory.utils.base64ToBitmap

import com.huangrui.dormitory.utils.hasDigest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MinePage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: MineViewModel = hiltViewModel()
) {
    val viewStates = viewModel.viewStates
    var showingDialog by remember { mutableStateOf(false) }
    viewModel.dispatch(MineViewAction.GetStatus)
    val launcher = rememberLauncherForActivityResult(contract = object :
        ActivityResultContract<String, String>() {
        override fun parseResult(resultCode: Int, intent: Intent?): String {
            if (resultCode == Activity.RESULT_OK) {
                return intent?.getStringExtra("data") ?: ""
            }
            return ""
        }

        override fun createIntent(context: Context, input: String): Intent {
            return Intent(context, FaceLivenessExpActivity::class.java).apply {
                putExtra("data", input)
            }
        }
    }, onResult = { result -> //result 是ActivityResultContract<String, String>第二个泛型
        viewModel.dispatch(MineViewAction.UpdateFace("data:image/png;base64,$result"))
        viewModel.dispatch(MineViewAction.StartLocation)
    })
    val mLocationInstance: LocationInstance = LocationInstance(
        DormitoryApplication.CONTEXT,
        object :
            LocationInstance.MyLocationListener() {
            override fun onReceiveLocation(location: BDLocation?) {
                super.onReceiveLocation(location)
                location?.locationDescribe?.let { Log.d("TAG", it) }
                if (location?.locationDescribe?.let { hasDigest(it) } == true) {
                    viewModel.dispatch(MineViewAction.UpdateAddress(location.locationDescribe))
                    viewModel.dispatch(MineViewAction.GiveCard)
                }
            }
        })
    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is MineViewEvent.LogoutOK) {
                RouteUtils.navTo(navCtrl,RouteName.HOME_ENTRY)
            } else if (it is MineViewEvent.StartLocation) {
                mLocationInstance.start()
            } else if (it is MineViewEvent.CardOk) {
                mLocationInstance.stop()
            } else if (it is MineViewEvent.ErrorMessage) {
                Toast.makeText(DormitoryApplication.CONTEXT, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .height(54.dp)
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = {
                    Text(
                        "我的", fontSize = 22.sp, modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                },
            )
        },
        content = { it ->
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                if (viewStates.type == 0) {
                    Image(
                        painter = painterResource(id = R.drawable.card),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable() {
                                launcher.launch("")
                            },
                        contentDescription = "",
                        alignment = Alignment.Center,
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ok),
                        modifier = Modifier.fillMaxWidth(),
                        contentDescription = "",
                        alignment = Alignment.Center,
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(onClick = {
                    showingDialog = true
                }, modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                ) {
                    Text(text = "留言反馈", fontSize = 22.sp, color = Color.White)
                }
                if (showingDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showingDialog = false
                        },

                        title = {
                            Text(text = "留言")
                        },
                        text = {
                            OutlinedTextField(value = viewStates.sug, onValueChange = {
                                viewModel.dispatch(MineViewAction.UpdateSug(it))
                            },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        text = "请输入反馈",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(top = 3.dp)
                                    )
                                }
                            )
                        },

                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.dispatch(MineViewAction.GiveSug)
                                    showingDialog = false
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text("确认提交")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showingDialog = false
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text("下次再说")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(onClick = {
                   viewModel.dispatch(MineViewAction.Logout)
                }, modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "退出登录", fontSize = 22.sp, color = Color.White)
                }
            }
        }
    )
}
