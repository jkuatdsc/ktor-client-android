package com.otienosamwel.ktor_client_android.data.remote

import com.otienosamwel.ktor_client_android.KtorClientAndroidApplication
import com.otienosamwel.ktor_client_android.R

var clientId: String = KtorClientAndroidApplication.instance.getString(R.string.client_id)
var clientSecretKey: String = KtorClientAndroidApplication.instance.getString(R.string.client_secret)
const val tokenUri = "https://oauth2.googleapis.com/token"
