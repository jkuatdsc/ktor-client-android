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
import io.ktor.client.statement.*
import io.ktor.http.*

object NetworkService {
    private const val BASE_URL = "jsonplaceholder.typicode.com"

    private val client = HttpClient(CIO) {

        defaultRequest {
            host = BASE_URL
            contentType(ContentType.Application.Json)
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
            })
        }

        install(Auth) {
            basic {
                sendWithoutRequest {true }
                credentials {
                    BasicAuthCredentials(username = "sam", password = "password")
                }
            }
        }
    }

    suspend fun makeReq(): HttpStatusCode {
        val res: HttpResponse = client.get("/posts")
        return res.status
    }

    suspend fun makeReq2(): List<Post> {
        val res: List<Post> = client.get()
        Log.i("Network", "getPosts: $res ")
        return res
    }

    suspend fun makeReq3() {
        client.post<Post> {
            contentType(ContentType.Application.Json)
            body = Post(body = "body", id = 2, title = "title", userId = 23)
        }
    }
}