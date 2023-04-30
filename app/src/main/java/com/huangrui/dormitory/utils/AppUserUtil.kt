package com.huangrui.dormitory.utils



object AppUserUtil {
    private const val LOGGED_FLAG = "logged_flag"
    private const val TOKEN_FLAG = "token_flag"

    var isLogged: Boolean
        get() = DataStoreUtils.readBooleanData(LOGGED_FLAG, false)
        set(value) = DataStoreUtils.saveSyncBooleanData(LOGGED_FLAG, value = value)

    var token: String
        get() = DataStoreUtils.readStringData(TOKEN_FLAG, "")
        set(value) = DataStoreUtils.saveSyncStringData(TOKEN_FLAG, value = value)

    fun onLogin() {
        isLogged = true
    }

    fun onLogOut() {
        isLogged = false
    }

}
