package com.example.mealmates.apiCalls

import com.example.mealmates.models.Group
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
class GroupApi {

    private val host: String = "http://10.0.2.2:8080"
    private val nullGroup = Group(-1, "", emptyList())
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

    fun getGroup(id: String): Group {
        return try {
            var group: Group? = null
            runBlocking {
                launch {
                    group = client.get("$host/group?id=$id").body()
                }
            }
            if (group != null) {
                group as Group
            } else {
                nullGroup
            }
        } catch (e: Exception) {
            throw e
        }
    }

    @OptIn(InternalAPI::class)
    fun createGroup(group: Group): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Adding group: $group")
                    success = client.post("$host/group") {
                        contentType(ContentType.Application.Json)
                        setBody(group)
                    }.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    @OptIn(InternalAPI::class)
    fun updateGroup(group: Group): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    println("Updating group: $group")
                    success = client.put("$host/group") {
                        contentType(ContentType.Application.Json)
                        setBody(group)
                    }.status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    @OptIn(InternalAPI::class)
    fun deleteGroup(id: String): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    success = client.delete("$host/group?id=$id").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

    @OptIn(InternalAPI::class)
    fun deleteUserFromGroup(uid: String, gid: String): Boolean {
        return try {
            var success = false
            runBlocking {
                launch {
                    success = client.delete("$host/group/user?uid=$uid&gid=$gid").status.isSuccess()
                }
            }
            success
        } catch (e: Exception) {
            throw e
        }
    }

}