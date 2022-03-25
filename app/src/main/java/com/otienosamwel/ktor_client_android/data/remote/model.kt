package com.otienosamwel.ktor_client_android.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)


@Serializable
data class TokenInfo(
    @SerialName("access_token") val accessToken: String?,
    @SerialName("expires_in") val expiresIn: Int?,
    @SerialName("refresh_token") val refreshToken: String? = null,
    val scope: String?,
    @SerialName("token_type") val tokenType: String?,
    @SerialName("id_token") val idToken: String?,
)

@Serializable
data class AccessTokenRequest(
    @SerialName("grant_type") val grantType: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("client_secret") val clientSecret: String,
    val code: String
)

@Serializable
data class RefreshTokenRequest(
    @SerialName("grant_type") val grantType: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("refresh_token") val refreshToken: String,
)

//email ids
@Serializable
data class EmailMessageIds(val messages: List<Id>)

@Serializable
data class Id(val id: String, val threadId: String)