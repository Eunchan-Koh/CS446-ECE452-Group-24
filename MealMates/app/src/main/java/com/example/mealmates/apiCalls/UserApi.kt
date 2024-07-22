package com.example.mealmates.apiCalls

import com.example.mealmates.models.Group
import com.example.mealmates.models.User
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

@OptIn(DelicateCoroutinesApi::class)
class UserApi {

    private val host: String = "http://10.0.2.2:8080"
    private val nullUser = User()
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            ContentType.Application.Json
        }
    }

    fun getUser(id: String): User {
        return try {
            var user: User? = null
            runBlocking {
                launch {
                    user = client.get("$host/user?id=$id").body()
                }
            }
            if (user != null) {
                user as User
            } else {
                nullUser
            }
        } catch (e: Exception) {
            throw e
        }
    }

    @OptIn(InternalAPI::class)
    fun addUser(user: User): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Adding user: $user")
                    success = client.post("$host/user") {
                        contentType(ContentType.Application.Json)
                        setBody(user)
                    }.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun updateUser(user: User): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    success = client.put("$host/user") {
                        contentType(ContentType.Application.Json)
                        setBody(user)
                    }.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    fun getUserGroups(id: String): List<Group> {
        return try {
            var groups: List<Group>? = null
            runBlocking {
                launch {
                    groups = client.get("$host/user/groups?id=$id").body()
                }
            }
            println("these are the groups $groups")
            if (groups != null) {
                groups as List<Group>
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun getUserByEmail(email: String): User {
        return try {
            var user: User? = null
            runBlocking {
                launch {
                    user = client.get("$host/user?email=$email").body()
                }
            }
            if (user != null) {
                user as User
            } else {
                nullUser
            }
        } catch (e: Exception) {
            throw e
        }
    }

}