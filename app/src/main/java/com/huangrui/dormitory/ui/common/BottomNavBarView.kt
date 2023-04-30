package com.huangrui.dormitory.ui.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBarView(navCtrl: NavHostController) {
    val bottomNavList = listOf(
        BottomNavRoute.Home,
        BottomNavRoute.InformationSystem,
        BottomNavRoute.Repair,
        BottomNavRoute.Mine,

    )
    NavigationBar() {
        val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavList.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(screen.stringId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.routeName } == true,
                onClick = {
                    Log.d("ROUTE","BottomNavView当前路由 ===> ${currentDestination?.hierarchy?.toList()}")
                    Log.d("ROUTE","当前路由栈 ===> ${navCtrl.graph.nodes}")
                    if (currentDestination?.route != screen.routeName) {
                        navCtrl.navigate(screen.routeName) {
                            popUpTo(navCtrl.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}
