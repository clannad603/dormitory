package com.huangrui.dormitory.ui.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.huangrui.dormitory.ui.bindRoom.BindRoomPage
import com.huangrui.dormitory.ui.common.AppScaffold
import com.huangrui.dormitory.ui.common.LoginRoute
import com.huangrui.dormitory.ui.common.RouteName
import com.huangrui.dormitory.ui.login.register.RegisterPage
import com.huangrui.dormitory.ui.mine.MinePage

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun LoginHostPage(
){
    val navCtrl = rememberNavController()
    NavHost(navController = navCtrl, startDestination = LoginRoute.LOGIN){
        composable(LoginRoute.APP_SCAFFOLD){
            AppScaffold()
        }
        composable(LoginRoute.LOGIN){
            LoginPage(navCtrl)
        }
        composable(LoginRoute.FACE_LOGIN){
            FaceLoginPage(navCtrl)
        }
        composable(LoginRoute.REGISTER){
            RegisterPage(navCtrl = navCtrl)
        }
        composable(route = LoginRoute.BINDROOM+"/{from}", arguments = listOf(navArgument("from") { type = NavType.StringType })){ it->
            val from = it.arguments?.getString("from")
            if (from != null) {
                BindRoomPage(navCtrl,from)
            }
        }
    }
}