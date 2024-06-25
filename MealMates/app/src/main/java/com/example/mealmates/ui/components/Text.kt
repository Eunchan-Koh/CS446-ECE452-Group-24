package com.example.mealmates.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mealmates.ui.theme.MealMatesTheme

@Composable
fun TitleLarge(text: String) {
    MealMatesTheme {
        Text(
            text,
            modifier = Modifier.padding(top = 13.dp, bottom = 5.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black
            )
        )
    }
}

@Composable
fun HeadlineLarge(text: String) {
    MealMatesTheme {
        Text(
            text,
            modifier = Modifier.padding(top = 13.dp, bottom = 5.dp),
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun HeadLineMedium(text: String) {
    MealMatesTheme {
        Text(
            text,
            modifier = Modifier.padding(top = 13.dp, bottom = 15.dp),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun BoldText(text: String) {
    MealMatesTheme {
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
