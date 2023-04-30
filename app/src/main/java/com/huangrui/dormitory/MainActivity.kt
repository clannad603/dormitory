package com.huangrui.dormitory

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.baidu.idl.face.platform.FaceSDKManager
import com.baidu.idl.face.platform.listener.IInitCallback
import com.baidu.idl.face.platform.utils.DensityUtils
import com.baidu.location.BDLocation
import com.baidu.mshield.x0.EngineImpl.mContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.huangrui.dormitory.initial.LocationInstance
import com.huangrui.dormitory.test.TestActivity
import com.huangrui.dormitory.ui.common.HomeEntry
import com.huangrui.dormitory.ui.face.FaceLivenessExpActivity
import com.huangrui.dormitory.ui.splash.SplashPage
import com.huangrui.dormitory.ui.theme.DormitoryTheme
import com.huangrui.dormitory.utils.base64ToBitmap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isInitFaceOK = false
    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLicense()
        setContent {
            HomeEntry()
        }

    }


    private fun initLicense(){

        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(mContext, "huangrui-dormitory-face-android",
            "idl-license.face-android", object : IInitCallback {
                override fun initSuccess() {
                    runOnUiThread {
                        Log.e("dormitory", "初始化成功")
                        isInitFaceOK = true
                    }
                }

                override fun initFailure(errCode: Int, errMsg: String) {
                    runOnUiThread {
                        Log.e(
                            "dormitory",
                            "初始化失败 = $errCode $errMsg"
                        )
                        isInitFaceOK = false
                    }
                }
            })
    }
}
