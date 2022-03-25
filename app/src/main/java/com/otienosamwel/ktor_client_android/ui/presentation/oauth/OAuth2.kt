package com.otienosamwel.ktor_client_android.ui.presentation.oauth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otienosamwel.ktor_client_android.util.SharedPrefUtil

@Composable
fun OAuth(login: () -> Unit, getEmail: () -> Unit) {
    val prefs = SharedPrefUtil
    val viewModel: OAuth2ViewModel = viewModel()
    val emails by viewModel.emails.observeAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "OAuth Activity", style = MaterialTheme.typography.h3)

        Spacer(modifier = Modifier.height(20.dp))

        if (prefs.getUid() == null) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = login) { Text(text = "Login with google") }
            }
        } else {
            Text(text = "You are logged in:", style = MaterialTheme.typography.h5)
            Column() {

                Text(text = "accessToken: ${prefs.getAccessToken()}")

                Spacer(modifier = Modifier.height(25.dp))

                Text(text = "refreshToken: ${prefs.getRefreshToken()}")

                Button(onClick = getEmail) {
                    Text(text = "Make authenticated Request")
                }
                emails?.let { email ->
                    Text(text = email)
                }
            }
        }
    }
}
