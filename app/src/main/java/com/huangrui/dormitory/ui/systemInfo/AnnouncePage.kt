package com.huangrui.dormitory.ui.systemInfo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import androidx.paging.compose.items
import com.google.accompanist.placeholder.material.placeholder
import com.huangrui.dormitory.ui.common.RouteName

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AnnouncePage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    announceViewModel: AnnounceViewModel = hiltViewModel()
) {
    announceViewModel.dispatch(AnnounceViewAction.OnStart)
    val pagingData = announceViewModel.viewStates.pagingData?.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            modifier = Modifier.height(54.dp).fillMaxWidth(),
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            title = {
                Text(
                    "系统通知", fontSize = 22.sp, modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            },
        )
        LazyColumn(
        ){
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(color = MaterialTheme.colorScheme.primary),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment =  Alignment.CenterVertically
                ) {
                    Text(text = "标题", fontSize = 22.sp)
                    Text(text = "发布者", fontSize = 22.sp, )
                    Text(text = "发布时间", fontSize = 22.sp)
                }
            }
                items(pagingData!!){it->
                    Box(
                        Modifier
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(5.dp)
                            )
                            .padding(start = 10.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable {
                                    navCtrl.navigate(RouteName.ANNOUNCE_CONTENT+"/"+it?.id)
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment =  Alignment.CenterVertically
                        ) {
                            Text(text = it!!.title, fontSize = 16.sp,)
                            Text(text = it.publisher, fontSize = 16.sp)
                            Text(text = it.publishTime, fontSize = 16.sp)
                        }
                    }
                }
        }

    }

}