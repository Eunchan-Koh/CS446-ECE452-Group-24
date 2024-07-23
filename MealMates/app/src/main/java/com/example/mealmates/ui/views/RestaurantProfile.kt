package com.example.mealmates.ui.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.R
import com.example.mealmates.apiCalls.GroupApi
import com.example.mealmates.apiCalls.RestaurantsApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.constants.RestaurantTypeToLabel
import com.example.mealmates.models.Group
import com.example.mealmates.models.Matched
import com.example.mealmates.models.MealMatesPlace
import com.example.mealmates.models.Restaurants
import com.example.mealmates.models.SearchNearbyRequest
import com.example.mealmates.models.SearchNearbyResponse
import com.example.mealmates.ui.theme.md_theme_light_errorContainer
import com.example.mealmates.ui.theme.md_theme_light_onTertiary
import com.example.mealmates.ui.theme.md_theme_light_tertiary
import com.example.mealmates.ui.theme.star_color
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.math.min
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

const val MAX_RESULT_COUNT = 10

fun fetchNearbyRestaurants(groupId: String): List<MealMatesPlace> {
    val groupInfo: Group = GroupApi().getGroup(groupId)
    if (groupInfo.preferences.isEmpty()) {
        return emptyList()
    }

    val request =
        SearchNearbyRequest(
                includedTypes = groupInfo.preferences,
                excludedTypes = emptyList(),
                maxResultCount = MAX_RESULT_COUNT,
                center = groupInfo.location,
                radius = 1000.0)
            .request

    val nearbySearchBodyString = Gson().toJson(request)
    val nearbySearchResponseString = searchNearbyMatches(requestBodyString = nearbySearchBodyString)
    val response = SearchNearbyResponse(nearbySearchResponseString)
    return response.listPlaces()
}

fun updateDatabaseOnLikeCompletion(
    userId: String,
    groupId: String,
    allRestaurants: List<MealMatesPlace>,
    likedRestaurants: List<String>
) {
    val res: Restaurants = RestaurantsApi().getRestaurants(groupId)
    var rids = mutableListOf<String>()
    var liked = mutableListOf<Int>()
    var completed = mutableListOf<String>()

    // Table row already exists
    if (res.rid != -1) {
        val matchedInfo: Matched = Gson().fromJson(res.matched.toString(), Matched::class.java)

        // User has already completed the matching process, skip
        if (matchedInfo.completed.contains(userId)) {
            println("User $userId has already completed the matching process. Skipping...")
            return
        }

        // Initialize with existing row data
        rids = matchedInfo.rids.toMutableList()
        liked = matchedInfo.liked.toMutableList()
        completed = matchedInfo.completed.toMutableList()
    }

    for (place in allRestaurants) {
        val index = rids.indexOf(place.id)
        // Restaurant is not in the list yet
        if (index == -1) {
            rids.add(place.id)
            liked.add(if (likedRestaurants.contains(place.id)) 1 else 0)
        } else {
            // Restaurant exists, update
            liked[index] += if (likedRestaurants.contains(place.id)) 1 else 0
        }
    }
    completed.add(userId)

    val updatedMatched =
        JsonObject(
            mapOf(
                "rids" to Json.encodeToJsonElement(rids),
                "liked" to Json.encodeToJsonElement(liked),
                "completed" to Json.encodeToJsonElement(completed)))

    // Table row does not exist, so create
    if (res.rid == -1) {
        // TODO: RID is hardcoded. Replace api call once the parameter is fixed
        val updatedInfo = Restaurants(0, groupId.toInt(), updatedMatched, emptyList())
        RestaurantsApi().addRestaurants(updatedInfo)
    } else {
        // Table row exists, so update
        val updatedInfo = Restaurants(res.rid, res.gid, updatedMatched, res.suggested)
        RestaurantsApi().editRestaurants(updatedInfo)
    }
}

@Composable
fun RestaurantPrompt(
    loginModel: LoginViewModel,
    onNavigateToMatchedRestaurants: () -> Unit,
    groupId: String
) {
    var isLoading by remember { mutableStateOf(true) }
    var places by remember { mutableStateOf(emptyList<MealMatesPlace>()) }
    var index by remember { mutableIntStateOf(0) }
    var likedRestaurants by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(Unit) {
        places = fetchNearbyRestaurants(groupId)
        isLoading = false
    }

    if (isLoading) {
        // TODO: Show loading spinner
        println("Restaurant prompt page is loading...")
        return
    }

    if (places.isEmpty()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
            Text(
                text = "Sorry, no nearby restaurants meet the groups preferences.",
                modifier =
                    Modifier.padding(16.dp).wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }
        return
    }

    fun onDislike() {
        index++
    }

    fun onLike() {
        likedRestaurants += places[index].id
        index++
    }

    Box(contentAlignment = Alignment.CenterStart) {
        // Liking complete
        if (index == places.size) {
            updateDatabaseOnLikeCompletion(
                GlobalObjects.user.id!!, groupId, places, likedRestaurants)
            onNavigateToMatchedRestaurants()
        } else {
            RestaurantProfile(places[index], { onDislike() }, { onLike() })
        }
    }
}

fun convertImageByteArrayToBitmap(imageData: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
}

fun fetchPlacePhoto(photoReference: String): ByteArray {
    val client = HttpClient(Android)
    val apiKey = "AIzaSyAjTN0RQCtZ3sWV6g_bw-D75cZkk6bmL3s"

    return try {
        var response = byteArrayOf()
        runBlocking {
            launch {
                val res: ByteArray =
                    client
                        .get("https://places.googleapis.com/v1/$photoReference/media") {
                            parameter("maxHeightPx", 2000)
                            parameter("key", apiKey)
                        }
                        .body()
                response = res
            }
        }
        response
    } catch (e: Exception) {
        throw e
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarouselBottomRow(
    pagerState: PagerState,
    onDislike: () -> Unit = {},
    onLike: () -> Unit = {}
) {
    Row(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().offset(y = -(32).dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = onDislike,
                modifier = Modifier.padding(start = 8.dp).size(45.dp).offset(y = -(22).dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_tertiary),
                contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Default.Clear, "dislike", tint = md_theme_light_onTertiary)
                }
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.Blue else Color.LightGray
                    Box(
                        modifier =
                            Modifier.padding(2.dp).clip(CircleShape).background(color).size(10.dp))
                }
            }
            Button(
                onClick = onLike,
                modifier = Modifier.padding(end = 8.dp).size(45.dp).offset(y = -(22).dp),
                shape = CircleShape,
                colors =
                    ButtonDefaults.buttonColors(containerColor = md_theme_light_errorContainer),
                contentPadding = PaddingValues(0.dp)) {
                    Icon(Icons.Default.FavoriteBorder, "like", tint = Color.Black)
                }
        }
}

const val MAX_IMAGES = 8
const val MAX_LABELS = 4

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun RestaurantProfile(place: MealMatesPlace, onDislike: () -> Unit, onLike: () -> Unit) {
    val photoCache = remember { mutableMapOf<String, ByteArray>() }

    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = { min(place.photos.size, MAX_IMAGES) })
        HorizontalPager(
            state = pagerState,
            key = { place.photos[it].photoReference },
            modifier = Modifier.weight(6f)) { index ->
                Box(modifier = Modifier.fillMaxSize()) {
                    val photoReference = place.photos[index].photoReference
                    var photoByteArray = photoCache[photoReference]
                    if (photoByteArray == null) {
                        photoCache[photoReference] = fetchPlacePhoto(photoReference)
                        photoByteArray = photoCache[photoReference]
                    }
                    Image(
                        bitmap = convertImageByteArrayToBitmap(photoByteArray!!).asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize())
                }
            }

        ImageCarouselBottomRow(pagerState, onDislike, onLike)

        Column(
            modifier =
                Modifier.weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .offset(y = -(30).dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = place.displayName, fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.padding(start = 4.dp)) {
                        Icon(Icons.Default.Star, "rating", tint = star_color)
                        Text(
                            text = place.rating.toString(),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 4.dp))
                    }

                    // OpenWebsiteButton(url = info.website)
                }

                // Remove city from address
                val address = place.shortFormattedAddress.split(",").dropLast(1).joinToString()
                Text(text = address, fontSize = 15.sp)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    for (type in place.types.take(MAX_LABELS)) {
                        if (RestaurantTypeToLabel[type] != null) {
                            SuggestionChip(
                                onClick = {},
                                label = { Text(RestaurantTypeToLabel[type]!!) },
                                modifier = Modifier.padding(end = 8.dp),
                            )
                        }
                    }
                }
            }
    }
}

@Composable
fun OpenWebsiteButton(url: String) {
    val context = LocalContext.current
    IconButton(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_open_in_new),
            contentDescription = "Open website")
    }
}
