package com.huangrui.dormitory.ui.repair.repairApply

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.baidu.idl.face.platform.FaceSDKManager
import com.baidu.idl.face.platform.utils.DensityUtils
import com.huangrui.dormitory.DormitoryApplication
import com.huangrui.dormitory.R
import com.huangrui.dormitory.ui.bindRoom.BindViewAction
import com.huangrui.dormitory.ui.common.RouteName
import com.huangrui.dormitory.ui.face.FaceLivenessExpActivity
import com.huangrui.dormitory.ui.login.facelogin.FaceLoginViewAction
import com.huangrui.dormitory.ui.pay.PayViewAction
import com.huangrui.dormitory.ui.repair.repairContent.RepairViewEvent
import com.huangrui.dormitory.utils.base64ToBitmap
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairApplyPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: RepairApplyViewModel = hiltViewModel()
) {
    var base64 by remember { mutableStateOf<Bitmap?>(null) }
    var isClick by remember{ mutableStateOf(false) }
    var selectType by remember{ mutableStateOf("请选择维修类型")}
    val viewStates = viewModel.viewStates
    LaunchedEffect(Unit){
        viewModel.viewEvents.collect{
            if (it is RepairApplyViewEvent.RepairApplyOK){
                navCtrl.navigate(RouteName.REPAIR)
            }else if (it is RepairApplyViewEvent.ErrorMessage){
                Toast.makeText(DormitoryApplication.CONTEXT,it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    val launcher = rememberLauncherForActivityResult(contract = object :
        ActivityResultContract<String, String>() {
        override fun parseResult(resultCode: Int, intent: Intent?): String {
            if (resultCode == Activity.RESULT_OK) {
                return intent?.getStringExtra("data") ?: ""
            }
            return ""
        }

        /**
         * @param compose向Compose中传的数据 ActivityResultContract<String, String>的第一个泛型
         */
        /**
         * @param compose向Compose中传的数据 ActivityResultContract<String, String>的第一个泛型
         */
        override fun createIntent(context: Context, input: String): Intent {
            return Intent(context, FaceLivenessExpActivity::class.java).apply {
                putExtra("data", input)
            }
        }
    }, onResult = { result -> //result 是ActivityResultContract<String, String>第二个泛型
        viewModel.dispatch(RepairApplyViewAction.UpdateFace("data:image/png;base64,$result"))

        var bmp: Bitmap? = base64ToBitmap(result)
        // 进行裁剪
        // 进行裁剪
        bmp = FaceSDKManager.getInstance().scaleImage(
            bmp,
            DensityUtils.dip2px(DormitoryApplication.CONTEXT, 97f),
            DensityUtils.dip2px(DormitoryApplication.CONTEXT, 97f)
        )
        base64 = bmp
    })
    Column(modifier = Modifier.fillMaxSize()) {
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
                Text( "维修申请", fontSize = 22.sp
                    ,modifier = Modifier
                        .fillMaxSize(0.8f).wrapContentSize(Alignment.Center))
            },
        )
        if (base64 == null) {
            Image(
                painter = painterResource(id = R.drawable.nosign),
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "",
                alignment = Alignment.Center,
            )
        } else {
            base64?.let { image ->
                Image(image.asImageBitmap(), null, modifier = Modifier.fillMaxWidth())
            }
        }
        Button(onClick = {
            launcher.launch("")
        },
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "点击进行录入脸部",fontSize = 22.sp, color = Color.White)
        }
        OutlinedTextField(
            value = viewStates.title,
            onValueChange = {viewModel.dispatch(RepairApplyViewAction.UpdateTitle(it))},
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            placeholder ={
                Text(
                    text = "请输入标题",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            value = viewStates.content,
            onValueChange = {viewModel.dispatch(RepairApplyViewAction.UpdateContent(it))},
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            placeholder ={
                Text(
                    text = "请输入维修内容",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        )
        OutlinedButton(
            onClick = { isClick = !isClick},
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = selectType ,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
        DropdownMenu(
            expanded = isClick,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = {},
            content = {
                viewStates.types.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(text = it.type)
                        }, onClick = {
                            isClick = !isClick
                            selectType= it.type
                            viewModel.dispatch(RepairApplyViewAction.UpdateType(it.id))
                        }
                    )
                }
            },
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Button(
            onClick = {
                viewModel.dispatch(RepairApplyViewAction.RepairApply)
            },
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "提交", fontSize = 22.sp, color = Color.White)
        }
    }
}