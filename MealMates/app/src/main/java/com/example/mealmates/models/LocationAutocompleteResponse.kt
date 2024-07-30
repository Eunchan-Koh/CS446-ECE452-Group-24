package com.example.mealmates.models

import android.util.Log
import com.example.mealmates.ui.views.getPlaceDetails
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonParser

data class AutocompletePlace(
    val placeId: String,
    val description: String,
    val location: LatLng
)

class LocationAutocompleteResponse(private var responseString: String) {
    fun getAutocompletePlaces(): List<AutocompletePlace> {
        try {
            val predictionsJSONObject = JsonParser.parseString(responseString).asJsonObject["predictions"]
            if (predictionsJSONObject == null || predictionsJSONObject.asJsonArray.asList().size == 0) {
                Log.e("invalid/empty LocationAutocompleteResponse", "empty `predictionsJSONObject` object")
                return listOf()
            }
            val responseList = mutableListOf<AutocompletePlace>()
            for (prediction in predictionsJSONObject.asJsonArray) {
                val predictionJSONObject = prediction.asJsonObject
                val placeId = predictionJSONObject["place_id"].asString
                val description = predictionJSONObject["description"].asString

                val placeResponse = getPlaceDetails(placeId)
                val location = GetPlaceDetailsResponse(placeResponse).getMealMatesPlace().location

                responseList.add(AutocompletePlace(placeId, description, location))
            }
            return responseList
        } catch (e: Exception) {
            throw e
        }
    }
}
