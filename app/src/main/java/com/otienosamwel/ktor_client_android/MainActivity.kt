package com.otienosamwel.ktor_client_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.otienosamwel.ktor_client_android.ui.presentation.oauth.OAuth2Activity
import com.otienosamwel.ktor_client_android.ui.theme.KtorclientandroidTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorclientandroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content()
                }
            }
        }
    }


    @Composable
    fun Content() {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(onClick = {
                viewModel.makeReq()
                Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Load")
            }


            Button(onClick = {
                val intent = Intent(this@MainActivity, OAuth2Activity::class.java)
                startActivity(intent)
            }) {
                Text(text = "Go to OAuth 2")
            }
        }
    }
}