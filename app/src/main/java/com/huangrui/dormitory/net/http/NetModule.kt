package com.huangrui.dormitory.net.http

import com.huangrui.dormitory.BuildConfig
import com.huangrui.dormitory.common.Constants.BASE_URL
import com.huangrui.dormitory.common.Constants.DEFAULT_TIMEOUT
import com.huangrui.dormitory.net.http.interceptor.LogInterceptor
import com.huangrui.dormitory.net.http.interceptor.TokenCookieInterceptor
import com.huangrui.dormitory.net.http.service.AnnounceService
import com.huangrui.dormitory.net.http.service.RepairService
import com.huangrui.dormitory.net.http.service.RoomService
import com.huangrui.dormitory.net.http.service.UserService
import com.huangrui.dormitory.utils.SSLSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideRepairService(retrofit: Retrofit):RepairService{
        return retrofit.create(RepairService::class.java)
    }


    @Singleton
    @Provides
    fun provideAnnounceService(retrofit: Retrofit): AnnounceService {
        return retrofit.create(AnnounceService::class.java)
    }

    @Singleton
    @Provides
    fun provideRoomService(retrofit: Retrofit): RoomService {
        return retrofit.create(RoomService::class.java)
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // 设置OkHttpclient
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(LogInterceptor())
        }
        builder.run {
            addNetworkInterceptor(TokenCookieInterceptor())
            connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getX509TrustManager())
            hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置

        }
        return builder.build()
    }
}