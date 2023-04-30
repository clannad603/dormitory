package com.huangrui.dormitory.ui.pay

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.huangrui.dormitory.DormitoryApplication
import com.huangrui.dormitory.ui.common.LoginRoute
import com.huangrui.dormitory.ui.common.RouteName
import com.huangrui.dormitory.ui.login.register.RegisterViewAction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayPage(
    navCtrl: NavHostController,
    id: String,
    type: String,
    viewModel: PayViewModel = hiltViewModel()
) {
    var typeId  by remember {
        mutableStateOf(0)
    }
    if (type =="power"){
        typeId = 1
    }
    viewModel.dispatch(PayViewAction.InitTypeAndId(typeId,id))
    val viewStates = viewModel.viewStates
    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {
            if (it is PayViewEvent.PayOK) {
                navCtrl.navigate(RouteName.HOME)
            } else if (it is PayViewEvent.ErrorMessage) {
                Toast.makeText(DormitoryApplication.CONTEXT, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.height(54.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            navigationIcon = {
                IconButton(onClick = {
                    navCtrl.navigateUp()
                }) {
                    Icon(Icons.Filled.ArrowBack, null)
                }
            },
            title = {
                Text( text = if (typeId==0) "请支付水费" else "请支付电费", fontSize = 22.sp
                    ,modifier = Modifier
                    .fillMaxSize(0.8f).wrapContentSize(Alignment.Center))
            },
        )
        OutlinedTextField(
            value = viewStates.pay,
            onValueChange = {viewModel.dispatch(PayViewAction.UpdateCost(it))},
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            placeholder ={
                Text(
                    text = "请输入充值数",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Button(
            onClick = {
                viewModel.dispatch(PayViewAction.Pay)
            },
            modifier = Modifier
                .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "支付", fontSize = 22.sp, color = Color.White)
        }
    }
}