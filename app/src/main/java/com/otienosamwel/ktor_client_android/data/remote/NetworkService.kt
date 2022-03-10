package com.otienosamwel.ktor_client_android.data.remote

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object NetworkService {
    private const val BASE_URL = "http://jsonplaceholder.typicode.com/posts"

    private val client = HttpClient(CIO) {

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
                credentials {
                    BasicAuthCredentials(username = "sam", password = "password")
                }
            }
        }
    }

    suspend fun makeReq(): HttpStatusCode {
        val res: HttpResponse = client.get(BASE_URL)
        return res.status
    }

    suspend fun makeReq2(): List<Post> {
        val res: List<Post> = client.get(BASE_URL)
        Log.i("Network", "getPosts: $res ")
        return res
    }

    suspend fun makeReq3() {
        client.post<Post>(BASE_URL) {
            contentType(ContentType.Application.Json)
            body = Post(body = "body", id = 2, title = "title", userId = 23)
        }
    }
}