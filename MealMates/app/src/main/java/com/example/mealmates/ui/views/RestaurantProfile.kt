package com.example.mealmates.ui.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.R
import com.example.mealmates.constants.RestaurantTypeToLabel
import com.example.mealmates.examples.PLACES_API_NEARBY_SEARCH_BODY_EXAMPLES
import com.example.mealmates.models.MealMatesPlace
import com.example.mealmates.models.SearchNearbyResponse
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

data class RestaurantInfo(
    val name: String,
    val address: String,
    val website: String,
    val photos: List<String>,
    val tags: List<String>
)

const val swipeThreshold = 300f

@Composable
fun RestaurantPrompt(
    loginModel: LoginViewModel,
    onNavigateToMatchedRestaurants: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }
    var places by remember { mutableStateOf(emptyList<MealMatesPlace>()) }
    var offset by remember { mutableFloatStateOf(0f) }
    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        val nearbySearchBodyString = Gson().toJson(PLACES_API_NEARBY_SEARCH_BODY_EXAMPLES[1])
        val nearbySearchResponseString =
            searchNearbyMatches(requestBodyString = nearbySearchBodyString)
        val response = SearchNearbyResponse(nearbySearchResponseString)
        places = response.listPlaces()
        isLoading = false
    }

    println("restaurnt prompt loop for no reason")

    if (isLoading) {
        // TODO: Show loading spinner
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

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier =
            Modifier.pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            offset > swipeThreshold -> {
                                println("swipe right")
                                index++
                            }
                            offset < -swipeThreshold -> {
                                println("swipe left")
                                index++
                            }
                        }
                    }) { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    }
            }) {
            if (index >= 1) {
                println("Done")
            } else {
                RestaurantProfile(places[index])
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

const val MAX_IMAGES = 8

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RestaurantProfile(place: MealMatesPlace) {
    val photoCache = remember { mutableMapOf<String, ByteArray>() }
    println(photoCache)
    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = { min(place.photos.size, MAX_IMAGES) })
        println("infinite loop check")
        HorizontalPager(
            state = pagerState,
            key = { place.photos[it].photoReference },
            modifier = Modifier.weight(6f)) { index ->
                Box(modifier = Modifier.fillMaxSize()) {
                    val photoReference = place.photos[index].photoReference
                    var photoByteArray = photoCache[photoReference]
                    if (photoByteArray == null) {
                        println("calling fetchplacephoto: " + photoReference)
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

        Row(
            Modifier.wrapContentHeight().fillMaxWidth().offset(y = -(32).dp),
            horizontalArrangement = Arrangement.Center) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.Blue else Color.LightGray
                    Box(
                        modifier =
                            Modifier.padding(2.dp).clip(CircleShape).background(color).size(10.dp))
                }
            }

        Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = place.displayName, fontWeight = FontWeight.Bold)
                // OpenWebsiteButton(url = info.website)
            }
            Text(text = place.shortFormattedAddress, fontSize = 15.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (type in place.types) {
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
