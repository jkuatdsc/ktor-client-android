package com.otienosamwel.ktor_client_android.data.remote

import android.util.Log
import com.otienosamwel.ktor_client_android.util.SharedPrefUtil
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object NetworkServiceAuth {

    private val preferences = SharedPrefUtil


    @OptIn(ExperimentalSerializationApi::class)
    private val tokenClient = HttpClient(CIO) {

        expectSuccess = false

        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(Json {
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

    suspend fun getInitialTokenInfo(): TokenInfo {
        return tokenClient.post(tokenUri) {
            setBody(
                AccessTokenRequest(
                    grantType = "authorization_code",
                    clientId = clientId,
                    clientSecret = clientSecretKey,
                    code = preferences.getAuthCode()!!
                )
            )
        }.body<TokenInfo>().also { tokenInfo ->
            tokenInfo.accessToken?.let { accessToken -> preferences.setAccessToken(accessToken = accessToken) }
            tokenInfo.refreshToken?.let { refreshToken -> preferences.setRefreshToken(refreshToken = refreshToken) }
        }
    }

    private suspend fun RefreshTokensParams.getRefreshTokenInfo(): TokenInfo {
        return tokenClient.post(tokenUri) {
            setBody(
                RefreshTokenRequest(
                    grantType = "refresh_token",
                    clientId = clientId,
                    refreshToken = preferences.getRefreshToken()!!
                )
            )
            markAsRefreshTokenRequest()
        }.body<TokenInfo>().also { tokenInfo ->
            tokenInfo.accessToken?.let { accessToken -> preferences.setAccessToken(accessToken = accessToken) }
            tokenInfo.refreshToken?.let { refreshToken -> preferences.setRefreshToken(refreshToken = refreshToken) }
        }
    }

    private val googleClient = HttpClient(CIO) {

        expectSuccess = false

        install(ContentNegotiation) {
            json(Json {
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

                    BearerTokens(
                        accessToken = preferences.getAccessToken()!!,
                        refreshToken = preferences.getRefreshToken() ?: ""
                    )
                }

                refreshTokens {
                    //get refresh tokens when 401 is encountered
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
        val data =
            googleClient.get("$baseUrl/${preferences.getUid()}/messages").body<EmailMessageIds>()
        val singleEmail = googleClient
            .get("$baseUrl/${preferences.getUid()}/messages/${data.messages[(0..2).random()].id}")
            .body<String>()

        Log.i("TAG", "getEmails: $singleEmail")
        return singleEmail
    }
}