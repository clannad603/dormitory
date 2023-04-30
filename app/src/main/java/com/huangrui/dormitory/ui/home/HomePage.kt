package com.huangrui.dormitory.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.huangrui.dormitory.R
import com.huangrui.dormitory.ui.common.RouteName
import com.huangrui.dormitory.ui.wigets.Banner
import com.huangrui.dormitory.ui.wigets.ImageBannerItemLocal

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalPagerApi
@Composable
fun HomePage(
    navCtrl: NavHostController,
    scaffoldState: Any?,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    homeViewModel.dispatch(HomeViewAction.Init)
    var viewStates = homeViewModel.viewStates
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.height(54.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            title = {
                Text(
                    text = buildAnnotatedString {
                        append("今天是校历第")
                        withStyle(
                            style = SpanStyle(color = Color(0xFFFFBB66)),
                        ) {
                            append("1")
                        }
                        append("周")
                    }, fontSize = 22.sp, modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(
                            Alignment.Center
                        )
                )
            },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            Banner(items = viewStates.images) {
                ImageBannerItemLocal(model = it)
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically){
            Icon(painter = painterResource(id = R.drawable.room), contentDescription ="" , modifier =Modifier.size(24.dp))
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontSize = 24.sp)
                ) {
                    append("我的寝室:")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFFFFBB66),
                        fontSize = 24.sp
                    ),
                ) {
                    append(viewStates.building + viewStates.room + "室")
                }
            }, modifier = Modifier.padding(5.dp))
        }
        Button(onClick = {
                navCtrl.navigate(RouteName.BIND_ROOM+"/home")
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            Text(text = "去绑定", fontSize = 22.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.padding(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(id = R.drawable.water), contentDescription = "", modifier = Modifier.size(22.dp))
                    Text("寝室水费", fontSize = 22.sp)
                }
                Text(text = "本月使用水量:", fontSize = 16.sp)
                Text(text = viewStates.waterAmount.toString() + " 吨", fontSize = 16.sp)
                Text(text = "本月使用水费:", fontSize = 16.sp)
                Text(text = viewStates.water.toString() + " 元", fontSize = 16.sp, color = if (viewStates.waterOverDue) Color.Red else Color.Black)
                if (viewStates.waterOverDue){
                    Button(onClick = {
                            navCtrl.navigate(RouteName.PAY+"?id=${viewStates.waterId}&type=water")
                    }, modifier = Modifier.width(100.dp)) {
                        Text(text = "去缴费", fontSize = 16.sp, color = Color.White)
                    }
                }

            }
            Column(Modifier.padding(10.dp)) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(id = R.drawable.power), contentDescription = "",modifier = Modifier.size(22.dp))
                    Text("寝室电费", fontSize = 22.sp)
                }
                Text(text = "本月使用电量:", fontSize = 16.sp)
                Text(text = viewStates.powerAmount.toString() + " 度", fontSize = 16.sp)
                Text(text = "本月使用电费:", fontSize = 16.sp)
                Text(text = viewStates.power.toString() + " 元", fontSize = 16.sp,color = if (viewStates.powerOverDue) Color.Red else Color.Black)
                if (viewStates.powerOverDue){
                    Button(onClick = {
                        navCtrl.navigate(RouteName.PAY+"?id=${viewStates.powerId}&type=power")
                    }, modifier = Modifier.width(100.dp) ) {
                        Text(text = "去缴费", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(painter = painterResource(id = R.drawable.grade), contentDescription = "",modifier = Modifier.size(22.dp))
            Text(text = "本月评分:  "+viewStates.score, fontSize = 22.sp)
            Icon(painter = painterResource(id = R.drawable.cup), contentDescription = "",modifier = Modifier.size(22.dp))
        }

    }
}