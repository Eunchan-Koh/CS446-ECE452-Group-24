package com.example.mealmates.ui.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mealmates.R
import com.example.mealmates.constants.RESTAURANT_DATA
import com.example.mealmates.ui.viewModels.LoginViewModel

data class RestaurantInfo(
    val name: String,
    val address: String,
    val website: String,
    val photos: List<String>,
    val tags: List<String>
)

const val swipeThreshold = 300f

@Composable
fun RestaurantPrompt(loginModel: LoginViewModel, onNavigateToMatchedRestaurants: () -> Unit) {
    var offset by remember { mutableStateOf(0f) }
    var index by remember { mutableStateOf(0) }

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
            if (index >= RESTAURANT_DATA.size) {
                onNavigateToMatchedRestaurants()
            } else {
                RestaurantProfile(info = RESTAURANT_DATA[index], tags = RESTAURANT_DATA[index].tags)
            }
        }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RestaurantProfile(info: RestaurantInfo, tags: List<String>) {
    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = { info.photos.size })

        HorizontalPager(
            state = pagerState, key = { info.photos[it] }, modifier = Modifier.weight(6f)) { index
                ->
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = info.photos[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
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
                Text(text = info.name, fontWeight = FontWeight.Bold)
                OpenWebsiteButton(url = info.website)
            }
            Text(text = info.address, fontSize = 15.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (tag in tags) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(tag) },
                        modifier = Modifier.padding(end = 8.dp))
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
