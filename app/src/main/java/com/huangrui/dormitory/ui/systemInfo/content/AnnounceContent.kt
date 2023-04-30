package com.huangrui.dormitory.ui.systemInfo.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnounceContent(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    id: String,
    announceContentViewModel: AnnounceContentViewModel = hiltViewModel()
) {
    announceContentViewModel.dispatch(AnnounceContentViewAction.InitId(id))
    announceContentViewModel.dispatch(AnnounceContentViewAction.GetContent)
    val viewState = announceContentViewModel.viewStates
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) ,
    ) {
        TopAppBar(
            modifier = Modifier
                .height(54.dp)
                .fillMaxWidth(),
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            navigationIcon = {
                IconButton(onClick = {
                    navCtrl.navigateUp()
                }) {
                    Icon(Icons.Filled.ArrowBack, modifier = Modifier.size(24.dp), contentDescription = "")
                }
            },
            title = {
                Text(
                    viewState.title, fontSize = 22.sp, modifier = Modifier
                        .fillMaxSize(0.8f)
                        .wrapContentSize(Alignment.Center)
                )
            },
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .padding(15.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Text(
                text = viewState.content, fontSize = 18.sp, modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 15.dp, end = 15.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "发布者", fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp))
                Text(text = viewState.publisher, fontSize = 18.sp, modifier = Modifier.padding(end = 16.dp))
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 15.dp, end = 15.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "发布时间", fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp))
                Text(text = viewState.publishTime, fontSize = 18.sp, modifier = Modifier.padding(end = 16.dp))
            }
        }
    }
}