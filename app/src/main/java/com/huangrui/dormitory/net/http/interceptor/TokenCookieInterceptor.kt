package com.huangrui.dormitory.net.http.interceptor

import com.huangrui.dormitory.utils.AppUserUtil.token
import okhttp3.Interceptor
import okhttp3.Response

class TokenCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Authorization", token)
        return chain.proceed(builder.build())
    }
}