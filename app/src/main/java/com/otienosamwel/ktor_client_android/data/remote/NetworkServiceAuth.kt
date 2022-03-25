package com.otienosamwel.ktor_client_android.data.remote

import android.util.Log
import com.otienosamwel.ktor_client_android.util.SharedPrefUtil
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.ExperimentalSerializationApi

object NetworkServiceAuth {

    private val preferences = SharedPrefUtil

    @OptIn(ExperimentalSerializationApi::class)
    private val tokenClient = HttpClient(CIO) {
        expectSuccess = false
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                isLenient = true
                prettyPrint = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }

    private suspend fun getTokenInfo(): TokenInfo {
        return tokenClient.post<TokenInfo>(tokenUri) {
            body = AccessTokenRequest(
                grantType = "authorization_code",
                clientId = clientId,
                clientSecret = clientSecretKey,
                code = preferences.getAuthCode()!!
            )
        }.also {
            it.accessToken?.let { it1 -> preferences.setAccessToken(it1) }
            it.refreshToken?.let { it1 -> preferences.setRefreshToken(it1) }
        }
    }

    private suspend fun getRefreshTokenInfo(): TokenInfo {
        return tokenClient.post<TokenInfo>(tokenUri) {
            body = RefreshTokenRequest(
                grantType = "refresh_token",
                clientId = clientId,
                refreshToken = preferences.getRefreshToken()!!
            )
        }.also {
            it.accessToken?.let { it1 -> preferences.setAccessToken(it1) }
            it.refreshToken?.let { it1 -> preferences.setRefreshToken(it1) }
        }
    }

    private val googleClient = HttpClient(CIO) {
        expectSuccess = false

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                isLenient = true
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                loadTokens {
                    getTokenInfo()
                    BearerTokens(
                        accessToken = preferences.getAccessToken()!!,
                        refreshToken = preferences.getRefreshToken() ?: "" //you can use a null assertion if you are certain you will receive the refresh token
                    )
                }
                refreshTokens {
                    getRefreshTokenInfo()
                    BearerTokens(
                        accessToken = preferences.getAccessToken()!!,
                        refreshToken = preferences.getRefreshToken()!!
                    )
                }
            }
        }
    }

    suspend fun getEmailIds(): String {
        val baseUrl = "https://gmail.googleapis.com/gmail/v1/users"
        val data = googleClient.get<EmailMessageIds>("$baseUrl/${preferences.getUid()}/messages")
        val singleEmail =
            googleClient.get<String>("$baseUrl/${preferences.getUid()}/messages/${data.messages[(0..2).random()].id}")
        Log.i("TAG", "getEmails: $singleEmail")
        return singleEmail
    }
}