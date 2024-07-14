package com.example.mealmates.models

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import org.json.JSONArray
import org.json.JSONObject


data class MealMatesPhoto(
    val widthPx: Int,
    val heightPx: Int,
    val photoReference: String      // not tested !!! might need modification during setting
)
data class MealMatesPlace (
    val id: String,
    val types: List<String>,
    val formattedAddress: String,
    val location: LatLng,
    val rating: Double,
    val displayName: String,
    val photos: List<MealMatesPhoto>
)
class SearchNearbyResponse(
    responseString: String
) {
    var placesJSON = JsonParser.parseString(responseString).asJsonObject["places"]

    fun listPlaces(): List<MealMatesPlace> {
        try {
            if (placesJSON == null || placesJSON.asJsonArray.asList().size == 0) {
                Log.e("invalid/empty SearchNearbyResponse", "empty `placesJSON` object")
                return listOf()
            }
            val responseList = mutableListOf<MealMatesPlace>()
            for (place in placesJSON.asJsonArray) {
                val placeJSONObject = place.asJsonObject
                val id = placeJSONObject["id"].asString
                val types = placeJSONObject["types"].asJsonArray
                val typesListOfStrings = mutableListOf<String>()
                for (type in types) {
                    typesListOfStrings.add(type.asString)
                }

                val formattedAddress = placeJSONObject["formattedAddress"].asString

                val locationJSONObject = placeJSONObject["location"].asJsonObject
                val location = LatLng(locationJSONObject["latitude"].asDouble, locationJSONObject["longitude"].asDouble)

                val rating = placeJSONObject["rating"].asDouble

                val displayName = placeJSONObject["displayName"].asJsonObject["text"].asString

                val photosList = mutableListOf<MealMatesPhoto>()
                if (placeJSONObject["photos"] != null) {
                    for (photo in placeJSONObject["photos"].asJsonArray) {
                        val photoJSONObject = photo.asJsonObject
                        val widthPx = photoJSONObject["widthPx"].asInt
                        val heightPx = photoJSONObject["heightPx"].asInt
                        val photoReference = photoJSONObject["name"].asString
                        photosList.add(MealMatesPhoto(widthPx, heightPx, photoReference))
                    }
                }

                responseList.add(MealMatesPlace(id, typesListOfStrings, formattedAddress, location, rating, displayName, photosList))
            }
            return responseList
        } catch (e: Exception) {
            throw e
        }
    }
}
