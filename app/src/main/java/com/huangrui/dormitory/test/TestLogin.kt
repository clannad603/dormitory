package com.huangrui.dormitory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview
//@Composable
//fun Login(){
//    Column(modifier = Modifier.fillMaxSize()) {
//            var state by remember {
//                mutableStateOf("0")
//            }
//           var other by remember {
//               mutableStateOf("1")
//           }
//            OutlinedTextField(
//                value = state,
//                onValueChange = {
//                      other = it
//                      state = it
//                },
//                modifier = Modifier.padding(start = 50.dp, top = 50.dp, end = 16.dp),
//                label = { Text(text = "这里是标签") },
//                placeholder = { Text(text = "请输入内容") },
//            )
//        OutlinedTextField(
//            value = other,
//            onValueChange = {
//
//            },
//            modifier = Modifier.padding(start = 50.dp, end = 16.dp, top = 50.dp),
//            label = { Text(text = "这里是标签") },
//            placeholder = { Text(text = "请输入内容") },
//        )
//        var jump by remember {
//            mutableStateOf(1)
//        }
//        Button(onClick = {  jump = 0}) {
//
//        }
//        if (jump==0){
//            Another()
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Another(){
//
//    Text(text = "hello", modifier = Modifier.fillMaxSize())
//}