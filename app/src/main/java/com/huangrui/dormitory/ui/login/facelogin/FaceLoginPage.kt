package com.huangrui.dormitory.ui.login

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.huangrui.dormitory.DormitoryApplication.Companion.CONTEXT
import com.huangrui.dormitory.R
import com.huangrui.dormitory.ui.common.LoginRoute
import com.huangrui.dormitory.ui.face.FaceLivenessExpActivity
import com.huangrui.dormitory.ui.login.facelogin.FaceLoginViewAction
import com.huangrui.dormitory.ui.login.facelogin.FaceLoginViewEvent
import com.huangrui.dormitory.ui.login.facelogin.FaceLoginViewModel
import com.huangrui.dormitory.utils.base64ToBitmap

@Composable
fun FaceLoginPage(
    navCtrl: NavHostController,
    viewModel: FaceLoginViewModel = hiltViewModel()
) {
    var base64 by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is FaceLoginViewEvent.LoginOK) {
                navCtrl.navigate(LoginRoute.APP_SCAFFOLD)
            } else if (it is FaceLoginViewEvent.ErrorMessage) {
                Toast.makeText(CONTEXT, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    val launcher = rememberLauncherForActivityResult(contract = object :
        ActivityResultContract<String, String>() {
        override fun parseResult(resultCode: Int, intent: Intent?): String {
            if (resultCode == RESULT_OK) {
                return intent?.getStringExtra("data") ?: ""
            }
            return ""
        }

        /**
         * @param compose向Compose中传的数据 ActivityResultContract<String, String>的第一个泛型
         */
        override fun createIntent(context: Context, input: String): Intent {
            return Intent(context, FaceLivenessExpActivity::class.java).apply {
                putExtra("data", input)
            }
        }
    }, onResult = { result -> //result 是ActivityResultContract<String, String>第二个泛型
        viewModel.dispatch(FaceLoginViewAction.UpdateAccount("data:image/png;base64,$result"))

        var bmp: Bitmap? = base64ToBitmap(result)
        // 进行裁剪
        // 进行裁剪
        bmp = FaceSDKManager.getInstance().scaleImage(
            bmp,
            DensityUtils.dip2px(CONTEXT, 97f),
            DensityUtils.dip2px(CONTEXT, 97f)
        )
        base64 = bmp
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .align(alignment = Alignment.Center)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = "",
                    alignment = Alignment.Center,
                )
                Spacer(modifier = Modifier.padding(15.dp))


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
                    Text(text = "点击进行录入脸部")
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "点击返回正常登录",  fontSize = 12.sp, color = Color.Gray,modifier = Modifier.clickable {
                    navCtrl.navigate(LoginRoute.LOGIN)
                }
                    .padding(start = 16.dp))
                Button(
                    onClick = {
                        viewModel.dispatch(FaceLoginViewAction.Login)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "请登录", color = Color.White)
                }
            }
        }
    }

}