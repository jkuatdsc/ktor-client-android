package com.otienosamwel.ktor_client_android.ui.presentation.oauth

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.otienosamwel.ktor_client_android.data.remote.clientId
import com.otienosamwel.ktor_client_android.ui.theme.KtorclientandroidTheme
import kotlinx.coroutines.launch

class OAuth2Activity : ComponentActivity() {

    private val viewModel: OAuth2ViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private var authCode: String? = null
    private var uid: String? = null


    private val startAuth = registerForActivityResult(StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        Log.i(
            TAG,
            "auth task : uid ${task.result.id} code ${task.result.serverAuthCode} all granted ${task.result.grantedScopes}"
        )
        try {
            val code = task.result.serverAuthCode!!
            val userId = task.result.id!!
            authCode = code
            uid = userId
            viewModel.loginUser(true)
            Log.d(TAG, "auth code: $code")

        } catch (e: ApiException) {
            Log.w(TAG, "auth code failed $e")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorclientandroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    OAuth(this::login)
                }
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestServerAuthCode(clientId)
            .requestId()
            .requestScopes(Scope("https://www.googleapis.com/auth/gmail.readonly"))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        viewModel.isLoggedIn.observe(this) {
            getUserInfo()
        }
    }

    private fun login() {
        val intent = googleSignInClient.signInIntent
        startAuth.launch(intent)
    }

    private fun getUserInfo() {
        lifecycleScope.launch {
            authCode?.let { code ->
                uid?.let { id ->
                    viewModel.getEmails(code, id)
                }
            }
        }
    }

    companion object {
        private const val TAG = "OAUTH"
    }
}