package com.huangrui.dormitory

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.baidu.location.LocationClient
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.huangrui.dormitory.utils.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DormitoryApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context
    }
    override fun onCreate() {
        super.onCreate()
        SDKInitializer.setAgreePrivacy(applicationContext,true);
        SDKInitializer.initialize(this)
        SDKInitializer.setCoordType(CoordType.BD09LL)
        LocationClient.setAgreePrivacy(true);
        CONTEXT = this
        DataStoreUtils.init(this)
    }

}