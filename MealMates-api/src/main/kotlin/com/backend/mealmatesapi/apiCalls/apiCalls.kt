package com.backend.mealmatesapi.apiCalls

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import org.postgresql.core.Tuple

//Here's an example on how to make 3rd party API calls in Kotlin using Ktor

// loads in information related to a single drug
//@OptIn(DelicateCoroutinesApi::class)
//fun callOpenFdaSingleDrug(drugId: String): JsonObject {
//    val client = HttpClient(Android)
//    try {
//        var data: JsonObject = JsonObject()
//        var res: String? = null
//        runBlocking{
//            launch{
//                val response: String = client.get("https://api.fda.gov/drug/label.json?search=id:$drugId&limit=1").bodyAsText()
//                // cast the response to a JsonObject of type SingleDrugRes
//                data = JsonParser.parseString(response).asJsonObject
//
//            }
//        }
//        return data
//    } catch(exception: Exception) {
//        println(exception);
//        return JsonObject()
//    }
//}
//
//fun callListOfMedication(strSoFar: String): MutableList<Pair<String, String>> {
//    val client = HttpClient(Android)
//    try {
//        val res = mutableListOf<Pair<String, String>>()
//        runBlocking{
//            launch{
//                val response: String = client.get("https://api.fda.gov/drug/label.json?search=openfda.brand_name:$strSoFar*&limit=20").body()
//                val parsedJson = JsonParser.parseString(response).asJsonObject
//                val results = parsedJson["results"].asJsonArray
//                for (result in results) {
//                    val drug = result.asJsonObject["openfda"].asJsonObject["brand_name"].asJsonArray[0].asString
//                    val id = result.asJsonObject["id"].asString
//                    if(!res.contains(Pair(drug, id))) {
//                        res.add(Pair(drug, id))
//                    }
//                }
//            }
//        }
//        return res
//    } catch(exception: Exception) {
//        return mutableListOf()
//    }
//}
