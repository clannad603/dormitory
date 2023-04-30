package com.huangrui.dormitory.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.huangrui.dormitory.ui.bindRoom.BindRoomPage
import com.huangrui.dormitory.ui.home.HomePage
import com.huangrui.dormitory.ui.login.LoginPage
import com.huangrui.dormitory.ui.mine.MinePage
import com.huangrui.dormitory.ui.pay.PayPage
import com.huangrui.dormitory.ui.repair.RepairPage
import com.huangrui.dormitory.ui.repair.repairApply.RepairApplyPage
import com.huangrui.dormitory.ui.repair.repairContent.RepairContentPage
import com.huangrui.dormitory.ui.systemInfo.AnnouncePage
import com.huangrui.dormitory.ui.systemInfo.content.AnnounceContent


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun AppScaffold() {
    val navCtrl = rememberNavController()
    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()
    Scaffold (
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
//        topBar = {
//            TopAppBar(
//                title = { Text("今天是2023年2月16日，校历第一周", fontSize = 18.sp, textAlign = TextAlign.Center) },
//                navigationIcon = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(Icons.Filled.Menu, null)
//                    }
//                }
//            )
//        },
        bottomBar = {
            when (currentDestination?.route) {
                    RouteName.HOME -> BottomNavBarView(navCtrl = navCtrl)
                    RouteName.INFORMATION_SYSTEM -> BottomNavBarView(navCtrl = navCtrl)
                    RouteName.REPAIR -> BottomNavBarView(navCtrl = navCtrl)
                    RouteName.MINE -> BottomNavBarView(navCtrl = navCtrl)
            }
        }
    ){
        NavHost(
            modifier = Modifier
                .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
                .padding(it),

            navController = navCtrl,
            startDestination = RouteName.HOME
        ) {
            //首页
            composable(route = RouteName.HOME) {
                HomePage(navCtrl, scaffoldState)
            }

            //系统通知
            composable(route = RouteName.INFORMATION_SYSTEM) {
                AnnouncePage(navCtrl, scaffoldState)
            }

            //我的
            composable(route = RouteName.MINE) {
                MinePage(navCtrl, scaffoldState)
            }

            //维修
            composable(route = RouteName.REPAIR) {
                RepairPage(navCtrl, scaffoldState)
            }

            composable(route = RouteName.REPAIR_APPLY) {
                RepairApplyPage(navCtrl, scaffoldState)
            }

            composable(RouteName.HOME_ENTRY){
                HomeEntry()
            }

            composable(route = RouteName.ANNOUNCE_CONTENT+"/{id}" ,
                arguments = listOf(navArgument("id"){
                    type = NavType.StringType
                }
            )){it->
                val id = it.arguments?.getString("id")
                if (id != null) {
                    AnnounceContent(navCtrl, scaffoldState,id)
                }
            }

            composable(route = RouteName.REPAIR_CONTENT+"/{id}" ,
                arguments = listOf(navArgument("id"){
                    type = NavType.StringType
                }
                )){it->
                val id = it.arguments?.getString("id")
                if (id != null) {
                    RepairContentPage(navCtrl, scaffoldState,id)
                }
            }

            composable(route = RouteName.BIND_ROOM+"/{from}", arguments = listOf(navArgument("from") { type = NavType.StringType })){ it->
                val from = it.arguments?.getString("from")
                if (from != null) {
                    BindRoomPage(navCtrl,from)
                }
            }

            composable(route = RouteName.PAY+"?id={id}&type={type}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType },
                    navArgument("type") { type = NavType.StringType },
                )){it->
                val id = it.arguments?.getString("id")
                val type = it.arguments?.getString("type")
                id?.let {
                    type?.let {
                        PayPage(navCtrl,id,type)
                    }
                }
            }
        }
    }
}