package com.huangrui.dormitory.utils

fun hasDigest(str:String):Boolean{
    for (i in str){
        if (i.isDigit()){
            return true
        }
    }
    return false
}