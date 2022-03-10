package com.otienosamwel.ktor_client_android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otienosamwel.ktor_client_android.data.remote.NetworkService
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    fun makeReq() {
        viewModelScope.launch {
            NetworkService.makeReq()
        }
    }

    fun getPosts() {
        viewModelScope.launch {
            NetworkService.makeReq2()
        }
    }

    fun sendPost() {
        viewModelScope.launch {
            NetworkService.makeReq3()
        }
    }
}