package com.huangrui.dormitory.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.huangrui.dormitory.DormitoryApplication.Companion.CONTEXT
import com.huangrui.dormitory.R
import com.huangrui.dormitory.ui.common.LoginRoute
import com.huangrui.dormitory.ui.login.login.LoginViewAction
import com.huangrui.dormitory.ui.login.login.LoginViewEvent
import com.huangrui.dormitory.ui.login.login.LoginViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun LoginPage(
    navCtrl: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val viewStates = viewModel.viewStates
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineState = rememberCoroutineScope()
    LaunchedEffect(Unit){
        viewModel.viewEvents.collect{
            if (it is LoginViewEvent.LoginOK){
                navCtrl.navigate(LoginRoute.APP_SCAFFOLD)
            }else if (it is LoginViewEvent.ErrorMessage){
                Toast.makeText(CONTEXT,it.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        keyboardController?.hide()
                    }
                )
            }
    ) {
        Card (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .align(alignment = Alignment.Center)
                .fillMaxWidth()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(15.dp),
        ){
            Column (modifier = Modifier.fillMaxWidth()){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription ="",
                    alignment = Alignment.Center,
                )
                Spacer(modifier = Modifier.padding(15.dp))
                OutlinedTextField(
                    value = viewStates.username,
                    onValueChange = {viewModel.dispatch(LoginViewAction.UpdateAccount(it))},
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    placeholder ={
                        Text(
                            text = "请输入用户名",
                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                )
                OutlinedTextField(
                    value = viewStates.password,
                    onValueChange = {viewModel.dispatch(LoginViewAction.UpdatePassword(it))},
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder ={
                        Text(
                            text = "请输入密码",
                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "试试人脸登录把",  fontSize = 12.sp, color = Color.Gray,modifier = Modifier.clickable {
                        navCtrl.navigate(LoginRoute.FACE_LOGIN)
                    }
                        .padding(start = 16.dp))
                    Text(text = "没有账号，请先进行注册",  fontSize = 12.sp, color = Color.Gray,modifier = Modifier.clickable {
                        navCtrl.navigate(LoginRoute.REGISTER)
                    }
                        .padding(end = 16.dp))
                }


                Button(
                    onClick = {
                        keyboardController?.hide()
                        viewModel.dispatch(LoginViewAction.Login)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = "请登录", color = Color.White)
                }
            }
        }
    }

}

