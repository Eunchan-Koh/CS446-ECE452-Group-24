package com.example.mealmates.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mealmates.R

data class RestaurantInfo(
    val name: String,
    val address: String,
    val website: String,
    val photos: List<String>
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RestaurantProfile(info: RestaurantInfo, tags: List<String>) {
    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = { info.photos.size })
        HorizontalPager(
            state = pagerState,
            // pageSize = PageSize.Fill,
            key = { info.photos[it] },
            modifier = Modifier.weight(5f)) { index ->
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = info.photos[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize())
                }
            }

        Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = info.name)
                OpenWebsiteButton(url = info.website)
            }
            Text(text = info.address, fontSize = 15.sp)
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
            painter =
                painterResource(id = R.drawable.ic_open_in_new), // Replace with your icon resource
            contentDescription = "Open website")
    }
}
