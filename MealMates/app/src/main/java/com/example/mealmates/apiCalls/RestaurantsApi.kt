package com.example.mealmates.apiCalls

import com.example.mealmates.models.Restaurants
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.InternalAPI
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.jsonArray

@OptIn(DelicateCoroutinesApi::class)
class RestaurantsApi {

    private val host: String = "https://mealmates-api-rdhyv35jla-uc.a.run.app"
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

    fun getRestaurants(gid: String): List<Restaurants> {
        return try {
            var restaurants: List<Restaurants>? = null
            runBlocking {
                launch {
                    restaurants = client.get("$host/restaurants?gid=$gid").body()
                    println("Fetched Restaurants: $restaurants")
                }
            }
            println("Restaurants: $restaurants")
            restaurants ?: listOf()
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
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

    @OptIn(InternalAPI::class)
    fun getCompletedRestaurants(gid: String): List<Restaurants> {
        return try {
            var restaurants: List<Restaurants>? = null
            runBlocking {
                launch {
                    restaurants = client.get("$host/restaurants/completed?gid=$gid").body()
                    println("Fetched Completed Restaurants: $restaurants")
                }
            }
            restaurants?.filter {
                val matchedObject = it.matched
                val completedArray = matchedObject["completed"]?.jsonArray
                val uidsArray = matchedObject["uids"]?.jsonArray
                (completedArray?.size ?: 0) >= (uidsArray?.size ?: 0)
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    @OptIn(InternalAPI::class)
    fun getSingleRestaurants(rid: String): Restaurants {
        return try {
            var restaurants: Restaurants? = null
            runBlocking {
                launch {
                    restaurants = client.get("$host/restaurants/single?rid=$rid").body()
                    println("Fetched Single Restaurants: $restaurants")
                }
            }
            println("Single Restaurants: $restaurants")
            restaurants ?: nullRestaurants
        } catch (e: Exception) {
            e.printStackTrace()
            nullRestaurants
        }
    }

}
