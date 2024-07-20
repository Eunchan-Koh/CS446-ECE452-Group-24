package com.backend.mealmatesapi.apiCalls

import com.backend.mealmatesapi.constants.FieldMasks
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun searchNearbyMatches(requestBodyString: String): String {
    val client = HttpClient(Android)
    val apiKey = "AIzaSyAjTN0RQCtZ3sWV6g_bw-D75cZkk6bmL3s"
    return try {
        var responseString = ""
        runBlocking {
            launch {
                val res: String = client.post("https://places.googleapis.com/v1/places:searchNearby") {
                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json)
                        append("X-Goog-Api-Key", apiKey)
                        append("X-Goog-FieldMask", FieldMasks.SEARCH_NEARBY_DEFAULT)
                    }
                    setBody(requestBodyString)
                }.bodyAsText()
                responseString = res
            }
        }
        responseString
    } catch (e: Exception) {
        throw e
    }
}

fun getPlaceDetails(placeID: String): String {
    val client = HttpClient(Android)
    val apiKey = "AIzaSyAjTN0RQCtZ3sWV6g_bw-D75cZkk6bmL3s"
    return try {
        var responseString = ""
        runBlocking {
            launch {
                val res: String = client.get("https://places.googleapis.com/v1/${placeID}/") {
                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json)
                        append("X-Goog-Api-Key", apiKey)
                        append("X-Goog-FieldMask", FieldMasks.PLACE_DETAILS_DEFAULT)
                    }
                }.bodyAsText()
                responseString = res
            }
        }
        responseString
    } catch (e: Exception) {
        throw e
    }
}
