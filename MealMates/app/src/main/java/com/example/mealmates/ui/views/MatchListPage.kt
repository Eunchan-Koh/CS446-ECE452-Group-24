package com.example.mealmates.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.apiCalls.RestaurantsApi
import com.example.mealmates.models.Restaurants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.*
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.theme.completed_colour
import com.example.mealmates.ui.theme.component_colour
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.theme.primary_text_colour
import com.example.mealmates.ui.viewModels.LoginViewModel

@Composable
fun MatchListPage(loginViewModel: LoginViewModel, gid: Int, onNavigateToMatch: (Int) -> Unit = {}) {
    var completedMatches by remember { mutableStateOf<List<Restaurants>>(emptyList()) }
    var inProgressMatches by remember { mutableStateOf<List<Restaurants>>(emptyList()) }

    LaunchedEffect(gid) {
        withContext(Dispatchers.IO) {
            val api = RestaurantsApi()
            val allMatches = api.getRestaurants(gid.toString())
            val completed = api.getCompletedRestaurants(gid.toString())

            completedMatches = completed
            inProgressMatches = allMatches.filter { match ->
                !completed.any { it.rid == match.rid }
            }

            completedMatches = completedMatches.filter { it.rid != -1 }
            inProgressMatches = inProgressMatches.filter { it.rid != -1 }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SectionHeader("Completed Matches")
        LazyColumn (
        ) {
            items(completedMatches) { match ->
                println("This is the match: $match")
                MatchRow(match = match, status = "Completed", onClick = { onNavigateToMatch(match.rid) })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader("In Progress Matches")
        LazyColumn {
            items(inProgressMatches) { match ->
                MatchRow(match = match, status = "In Progress", onClick = {  })
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(button_colour, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun MatchRow(match: Restaurants, status: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
            .background(component_colour, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text("Match ID: ${match.rid}", fontSize = 16.sp, color = primary_text_colour)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(status, fontSize = 16.sp, color = if (status == "Completed") completed_colour else Color.Gray)
                Icon(
                    imageVector = if (status == "Completed") Icons.Default.Check else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (status == "Completed") completed_colour else Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
