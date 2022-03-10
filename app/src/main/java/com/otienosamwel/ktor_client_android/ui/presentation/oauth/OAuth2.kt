package com.otienosamwel.ktor_client_android.ui.presentation.oauth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otienosamwel.ktor_client_android.ui.presentation.oauth.model.Email

@Composable
fun OAuth(login: () -> Unit) {

    val viewModel: OAuth2ViewModel = viewModel()
    val loggedInText =
        if (viewModel.isLoggedIn.observeAsState().value == true) "You are logged in" else "Not yet logged in"
    val emails: List<Email>? by viewModel.emails.observeAsState()

    Column() {
        Text(text = "OAuth Activity")
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = loggedInText)
            Spacer(modifier = Modifier.weight(1f))
            if (viewModel.isLoggedIn.value != true) Button(onClick = login) {
                Text(text = "Login with google")
            }
        }

        if (viewModel.isLoggedIn.value == true) {
            Text(text = "Emails from authenticated google server request")
            LazyColumn {
                emails?.let { emails ->
                    items(emails) { email ->
                        Text(text = "$email")
                    }
                }
            }
        }
    }
}