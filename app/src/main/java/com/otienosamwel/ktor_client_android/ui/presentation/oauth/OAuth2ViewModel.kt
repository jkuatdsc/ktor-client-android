package com.otienosamwel.ktor_client_android.ui.presentation.oauth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otienosamwel.ktor_client_android.data.remote.NetworkServiceAuth
import kotlinx.coroutines.launch

class OAuth2ViewModel : ViewModel() {

    private val _emails = MutableLiveData<String>()
    val emails: LiveData<String> get() = _emails

    suspend fun getEmails() {
        _emails.value = NetworkServiceAuth.getEmailIds()
    }

    private var _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun loginUser(state: Boolean) {
        _isLoggedIn.value = state
    }

    fun loadInitialTokenRequest() {
        viewModelScope.launch {
            NetworkServiceAuth.getInitialTokenInfo()
        }
    }
}