package com.otienosamwel.ktor_client_android.data.remote

import android.util.Log
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

object NetworkServiceAuth {

    private val tokenClient = HttpClient(CIO) {

        defaultRequest {
            contentType(ContentType.Application.Json)
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                isLenient = true
                prettyPrint = true
            })

        }

        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }

    private fun createGoogleClient(authCode: String): HttpClient {
        val googleClient = HttpClient(CIO) {
            expectSuccess = false

            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    isLenient = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(Auth) {

                lateinit var tokenInfo: TokenInfo
                lateinit var refreshTokenInfo: TokenInfo

                bearer {
                    loadTokens {
                        tokenInfo = tokenClient.post(tokenUri) {
                            body = AccessTokenRequest(
                                grantType = "authorization_code",
                                clientId = clientId,
                                clientSecret = clientSecretKey,
                                code = authCode
                            )
                        }
                        BearerTokens(
                            accessToken = tokenInfo.accessToken,
                            refreshToken = tokenInfo.refreshToken!!
                        )
                    }

                    refreshTokens {
                        refreshTokenInfo = tokenClient.post(tokenUri) {
                            body = RefreshTokenRequest(
                                grantType = "refresh_token",
                                clientId = clientId,
                                refreshToken = tokenInfo.refreshToken!!
                            )
                        }
                        BearerTokens(
                            accessToken = refreshTokenInfo.accessToken,
                            refreshToken = tokenInfo.refreshToken!!
                        )
                    }
                }
            }
        }
        return googleClient
    }

    suspend fun getEmails(authorizationCode: String, userId: String): String {
        val data = createGoogleClient(authorizationCode).get<String>("https://gmail.googleapis.com/gmail/v1/users/$userId/messages")
        Log.i("TAG", "getEmails: $data")
        return data
    }


}