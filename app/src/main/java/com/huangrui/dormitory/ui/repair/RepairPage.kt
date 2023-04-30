package com.huangrui.dormitory.ui.repair

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.huangrui.dormitory.ui.common.RouteName
import com.huangrui.dormitory.ui.systemInfo.AnnounceViewAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RepairPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    repairViewModel: RepairViewModel = hiltViewModel()
) {
   repairViewModel.dispatch(RepairViewAction.OnStart)
    val pagingData = repairViewModel.viewStates.pagingData?.collectAsLazyPagingItems()
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .height(54.dp)
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = {
                    Text(
                        "维修", fontSize = 22.sp, modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                    navCtrl.navigate(RouteName.REPAIR_APPLY)
            },) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        },

        content = {it->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background)
            ) {
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
                            Text(text = "类型", fontSize = 22.sp, )
                            Text(text = "发布时间", fontSize = 22.sp)
                            Text(text = "状态", fontSize = 22.sp)
                        }
                    }
                    items(pagingData!!){it->
                            if (it!!.status!=-1){
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
                                                navCtrl.navigate(RouteName.REPAIR_CONTENT + "/" + it?.id)
                                            },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment =  Alignment.CenterVertically
                                    ) {
                                        Text(text = it!!.title, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.25f))
                                        Text(text = it!!.type, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.25f))
                                        Text(text = it!!.commitTime, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth(0.25f))
                                        if (it.status==0){
                                            Button(onClick = {}, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)) {
                                                Text(text = "未处理", fontSize = 16.sp)
                                            }
                                        }else{
                                            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                                                Text(text = "已处理", fontSize = 16.sp)
                                            }

                                        }
                                    }
                            }

                        }
                    }

                }
            }
        }
    )

}