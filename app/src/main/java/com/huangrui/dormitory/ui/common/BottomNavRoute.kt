package com.huangrui.dormitory.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.huangrui.dormitory.R

sealed class BottomNavRoute(
    var routeName: String,
    @StringRes var stringId: Int,
    var icon: ImageVector
) {
    object Home: BottomNavRoute(RouteName.HOME, R.string.home, Icons.Default.Home)
    object InformationSystem: BottomNavRoute(RouteName.INFORMATION_SYSTEM, R.string.information_system, Icons.Default.Menu)
    object Repair: BottomNavRoute(RouteName.REPAIR, R.string.repair, Icons.Default.Build)
    object Mine: BottomNavRoute(RouteName.MINE, R.string.mine, Icons.Default.Person)

}