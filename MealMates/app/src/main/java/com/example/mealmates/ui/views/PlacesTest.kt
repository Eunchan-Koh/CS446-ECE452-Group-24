package com.example.mealmates.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.constants.FieldMasks
import com.example.mealmates.examples.PLACES_API_NEARBY_SEARCH_BODY_EXAMPLES
import com.example.mealmates.models.MealMatesPlace
import com.example.mealmates.models.SearchNearbyResponse
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append


@Composable
fun PlacesTest(loginModel: LoginViewModel) {
    //AndroidPlacesAPIMethod(placesClient)

    // ktor
    val nearbySearchBodyString = Gson().toJson(PLACES_API_NEARBY_SEARCH_BODY_EXAMPLES[1])
    val nearbySearchResponseString = searchNearbyMatches(requestBodyString = nearbySearchBodyString)
    val response = SearchNearbyResponse(nearbySearchResponseString)
    val nearbySearchResults = response.listPlaces()
    PlacesResultDisplay(places = nearbySearchResults)
}

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
                        append("X-Goog-FieldMask", FieldMasks.DEFAULT)
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
        var fieldMasks = "id,displayName,types,photos,shortFormattedAddress,location,currentOpeningHours,priceLevel,rating,websiteUri"
        var responseString = ""
        runBlocking {
            launch {
                val res = client.get("https://places.googleapis.com/v1/places/${placeID}?fields=${fieldMasks}&key=${apiKey}")
                    .bodyAsText()
                responseString = res
            }
        }
        responseString
    } catch (e: Exception) {
        throw e
    }
}

fun locationAutocomplete(textInput: String): String {
    val client = HttpClient(Android)
    val apiKey = "AIzaSyAjTN0RQCtZ3sWV6g_bw-D75cZkk6bmL3s"
    return try {
        var responseString = ""
        runBlocking {
            launch {
                val res = client.get("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=${textInput}&key=${apiKey}")
                    .bodyAsText()
                responseString = res
            }
        }
        responseString
    } catch (e: Exception) {
        throw e
    }
}

/*@Composable
fun AndroidPlacesAPIMethod(placesClient: PlacesClient) {
    val numMembers = 5
    val placeFields = listOf(Place.Field.ID, Place.Field.NAME)
    val center = LatLng(40.7580, -73.9855)
    val circle = CircularBounds.newInstance(center,  *//* radius = *//*1000.0)
    val includedTypes = listOf("restaurant", "cafe")
    val searchNearbyRequest =
        SearchNearbyRequest.builder(*//* locationRestriction = *//* circle, placeFields)
            .setIncludedTypes(includedTypes)
            .setMaxResultCount(20)
            .build()
    val (places, setPlaces) = remember {
        mutableStateOf(listOf<Place>())
    }
    placesClient.searchNearby(searchNearbyRequest)
        .addOnSuccessListener { response: SearchNearbyResponse ->
            setPlaces(response.places)
        }
        .addOnFailureListener { exception: Exception ->
            println(exception.message)
        }
    PlacesResultDisplay(places = places)
}*/

@Composable
fun PlacesResultDisplay(places: List<MealMatesPlace>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val numPlaces = places.size
        for (i in 1..numPlaces) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    onClick = { /* todo */ },
                    colors = ButtonDefaults.buttonColors(md_theme_light_primary, Color.White, Color.Black, Color.White),
                    modifier = Modifier
                        .height(80.dp)
                        .width((LocalConfiguration.current.screenWidthDp * 0.80).dp),
                    shape = RectangleShape
                ) {
                    Text(places[i-1].displayName, color = Color.White, fontSize = 24.sp)
                }
            }
        }
    }
}
