package com.example.mealmates.models

import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonParser

class GetPlaceDetailsResponse(private var responseString: String) {
    fun getMealMatesPlace(): MealMatesPlace {
        try {
            println(responseString)
            val placeJSONObject = JsonParser.parseString(responseString).asJsonObject
                ?: throw Exception("invalid/empty GetPlaceDetailsResponse: empty `placeJSON` object")
            val id = placeJSONObject["id"].asString
            val types = placeJSONObject["types"].asJsonArray
            val typesListOfStrings = mutableListOf<String>()
            for (type in types) {
                typesListOfStrings.add(type.asString)
            }
            val shortFormattedAddress = placeJSONObject["shortFormattedAddress"].asString

            val locationJSONObject = placeJSONObject["location"].asJsonObject
            val location =
                LatLng(
                    locationJSONObject["latitude"].asDouble,
                    locationJSONObject["longitude"].asDouble)

            val rating =
                if (placeJSONObject["rating"] != null) placeJSONObject["rating"].asDouble
                else 0.0

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
            return MealMatesPlace(
                id,
                typesListOfStrings,
                shortFormattedAddress,
                location,
                rating,
                displayName,
                photosList)
        } catch (e: Exception) {
            throw e
        }
    }
}
