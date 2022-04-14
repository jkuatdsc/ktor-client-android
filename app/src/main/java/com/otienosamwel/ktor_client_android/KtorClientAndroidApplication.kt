package com.otienosamwel.ktor_client_android

import android.app.Application

class KtorClientAndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    companion object {
        lateinit var instance: Application
    }
}