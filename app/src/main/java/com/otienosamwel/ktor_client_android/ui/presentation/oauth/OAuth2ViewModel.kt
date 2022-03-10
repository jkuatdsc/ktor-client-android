package com.otienosamwel.ktor_client_android.ui.presentation.oauth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.otienosamwel.ktor_client_android.data.remote.NetworkService
import com.otienosamwel.ktor_client_android.data.remote.NetworkServiceAuth
import com.otienosamwel.ktor_client_android.ui.presentation.oauth.model.Email

class OAuth2ViewModel : ViewModel() {

    private val _emails = MutableLiveData<List<Email>>()
    val emails: LiveData<List<Email>> get() = _emails

    suspend fun getEmails(authorizationCode: String, userId: String) {
        NetworkServiceAuth.getEmails(authorizationCode, userId)
    }

    private var _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun loginUser(state: Boolean) {
        _isLoggedIn.value = state
    }
}