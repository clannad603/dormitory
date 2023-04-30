package com.huangrui.dormitory.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.huangrui.dormitory.ui.login.LoginHostPage
import com.huangrui.dormitory.ui.login.LoginPage
import com.huangrui.dormitory.ui.splash.SplashPage
import com.huangrui.dormitory.ui.theme.DormitoryTheme
import com.huangrui.dormitory.utils.AppUserUtil

@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun HomeEntry() {
    //是否闪屏页
    var isSplash by remember { mutableStateOf(true) }
    DormitoryTheme {
        if (isSplash) {
            SplashPage { isSplash = false }
        } else {
            if (AppUserUtil.isLogged){
                AppScaffold()
            }else{
                LoginHostPage()
            }
        }
    }
}

