package com.otienosamwel.ktor_client_android.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPrefUtil {

    private lateinit var preferences: SharedPreferences
    fun getContext(context: Context) {
        preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    fun setAuthCode(token: String) = preferences.set("token", token)
    fun getAuthCode(): String? = preferences.getString("token", null)

    fun setUid(uId: String) = preferences.set("uId", uId)
    fun getUid(): String? = preferences.getString("uId", null)

    fun setAccessToken(accessToken: String) = preferences.set("accessToken", accessToken)
    fun getAccessToken(): String? = preferences.getString("accessToken", null)

    fun setRefreshToken(refreshToken: String) = preferences.set("refreshToken", refreshToken)
    fun getRefreshToken(): String? = preferences.getString("refreshToken", null)

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is Int -> edit { this.putInt(key, value) }
            is Long -> edit { this.putLong(key, value) }
            is Float -> edit { this.putFloat(key, value) }
            is String? -> edit { this.putString(key, value) }
            is Boolean -> edit { this.putBoolean(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }
}