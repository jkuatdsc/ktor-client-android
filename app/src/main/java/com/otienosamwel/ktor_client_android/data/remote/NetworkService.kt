package com.otienosamwel.ktor_client_android.data.remote

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

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

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }

        install(Auth) {
            basic {
                sendWithoutRequest { true }
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
        val res: List<Post> = client.get { }.body()
        Log.i("Network", "getPosts: $res ")
        return res
    }

    suspend fun makeReq3() {
        client.post {
            contentType(ContentType.Application.Json)
            setBody(Post(body = "body", id = 2, title = "title", userId = 23))
        }
    }
}