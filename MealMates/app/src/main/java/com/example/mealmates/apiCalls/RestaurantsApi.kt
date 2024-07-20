package com.example.mealmates.apiCalls

import com.example.mealmates.models.Restaurants
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

@OptIn(DelicateCoroutinesApi::class)
class RestaurantsApi {

    private val host: String = "http://10.0.2.2:8080"
    private val nullRestaurants = Restaurants(-1, -1)
    private val client =
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                ContentType.Application.Json
            }
        }

    fun getRestaurants(gid: String): Restaurants {
        return try {
            var restaurants: Restaurants? = null
            runBlocking { launch { restaurants = client.get("$host/restaurants?gid=$gid").body() } }
            if (restaurants != null) {
                restaurants as Restaurants
            } else {
                nullRestaurants
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun addRestaurants(restaurants: Restaurants): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Adding restaurants: $restaurants")
                    success =
                        client
                            .post("$host/restaurants") {
                                contentType(ContentType.Application.Json)
                                setBody(restaurants)
                            }
                            .status
                            .isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun editRestaurants(restaurants: Restaurants): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Editing restaurants: $restaurants")
                    success =
                        client
                            .put("$host/restaurants") {
                                contentType(ContentType.Application.Json)
                                setBody(restaurants)
                            }
                            .status
                            .isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteRestaurants(gid: String): Boolean {
        return try {
            var success = false
            runBlocking {
                launch { success = client.delete("$host/restaurants?gid=$gid").status.isSuccess() }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }
}
