package com.huangrui.dormitory.ui.splash

import android.os.Build


import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.huangrui.dormitory.R
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashPage(onNextPage: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = androidx.compose.material3.MaterialTheme.colorScheme.primary),
        horizontalAlignment =  Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(Unit) {
            delay(1000)
            onNextPage.invoke()
        }
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "", modifier = Modifier.size(120.dp))
        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "寝室楼栋管理系统", color = Color.Black, fontSize = 20.sp)
        val permissionStates = rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.CHANGE_WIFI_STATE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
        )
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(key1 = lifecycleOwner, effect = {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            permissionStates.launchMultiplePermissionRequest()
                        }
                    }
                    else-> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        })
    }
}
