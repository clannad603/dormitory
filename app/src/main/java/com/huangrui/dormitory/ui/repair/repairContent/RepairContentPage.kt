package com.huangrui.dormitory.ui.repair.repairContent

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.huangrui.dormitory.DormitoryApplication
import com.huangrui.dormitory.ui.common.LoginRoute
import com.huangrui.dormitory.ui.common.RouteName
import com.huangrui.dormitory.ui.login.login.LoginViewEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairContentPage(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    id: String,
    viewModel: RepairContentViewModel = hiltViewModel()
) {
    viewModel.dispatch(RepairContentViewAction.InitId(id))
    viewModel.dispatch(RepairContentViewAction.GetContent)
    val viewState = viewModel.viewStates
    LaunchedEffect(Unit){
        viewModel.viewEvents.collect{
            if (it is RepairViewEvent.DeleteOK){
                navCtrl.navigate(RouteName.REPAIR)
            }else if (it is RepairViewEvent.ErrorMessage){
                Toast.makeText(DormitoryApplication.CONTEXT,it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                        .wrapContentSize(Alignment.Center),
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
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
                Text(text = "维修类型", fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp))
                Text(text = viewState.type, fontSize = 18.sp, modifier = Modifier.padding(end = 16.dp))
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
                Text(text = "提交时间", fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp))
                Text(text = viewState.commitTime, fontSize = 18.sp, modifier = Modifier.padding(end = 16.dp))
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        if (viewState.status==0){
            Button(onClick = {
                    viewModel.dispatch(RepairContentViewAction.DeleteRepair(viewState.id))
            }, modifier = Modifier.padding(start = 15.dp,end = 15.dp)) {
                Text(text = "点击撤销")
            }
        }
    }
}
