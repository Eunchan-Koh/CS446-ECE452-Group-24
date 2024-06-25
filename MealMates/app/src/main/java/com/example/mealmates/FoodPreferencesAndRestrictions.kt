package com.example.mealmates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.ui.theme.Purple40
import com.example.mealmates.ui.theme.Purple80


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PreferenceAndRestrictions() {
    val preferences = listOf(
        "Chinese", "Indian", "Italian", "Korean", "Taiwanese", "Thai", "Mediterranean",
        "Arab", "Greek", "Caribbean", "African", "Turkish", "Mexican", "Japanese", "French",
        "British", "Middle Eastern", "Lao", "Cajun", "Portuguese", "American", "Malaysian",
        "+",
    )

    val restrictions = listOf(
        "Vegan", "Halal", "Kosher", "Vegetarian", "Gluten-Free", "Pescatarian", "Paleo",
        "Dairy-Free", "+"
    )

    Column(
        Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    )
    {
        Column(
            Modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxSize()
                .background(Color.White)
                .padding(30.dp)
        ) {
            Text(
                text = "Food Preferences",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Purple40,
            )

            Spacer(modifier = Modifier.height(8.dp))
            SearchFieldWithSearchIcon();
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                Modifier
                    .fillMaxWidth(1f)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                maxItemsInEachRow = 4,
            ) {
                for (p in preferences) {
                    var selected by remember { mutableStateOf(false) }

                    FilterChip(
                        onClick = { selected = !selected },
                        label = {
                            Text(p)
                        },
                        selected = selected,
                        leadingIcon = if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                )
                            }
                        } else {
                            null
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))


            Text(
                text = "Food Restrictions",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Purple40,
            )

            Spacer(modifier = Modifier.height(8.dp))
            SearchFieldWithSearchIcon()
            Spacer(modifier = Modifier.height(8.dp))


            FlowRow(
                Modifier
                    .fillMaxWidth(1f)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                for (r in restrictions) {
                    var selected by remember { mutableStateOf(false) }

                    FilterChip(
                        onClick = { selected = !selected },
                        label = {
                            Text(r)
                        },
                        selected = selected,
                        leadingIcon = if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                )
                            }
                        } else {
                            null
                        },
                    )
                }
            }
        }
        SaveCancelButton()
    }
}

@Composable
fun SaveCancelButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        CancelButton();
        SaveChangesButton();
    }
}

@Composable
fun SearchFieldWithSearchIcon() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    return OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = text,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "searchIcon"
            )
        },
        onValueChange = {
            text = it
        },
        placeholder = {
            Text(text = "Search")
        },
    )
}

@Composable
fun SaveChangesButton() {
    val buttonColor = remember { mutableStateOf(Purple80) }
    Button(
        onClick = { /* Handle save changes */ },
        colors = ButtonDefaults.buttonColors(
            Purple40
        ),
        modifier = Modifier
            .height(50.dp),
        shape = CircleShape
    ) {
        Text(text = "Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CancelButton() {
    Button(
        onClick = { /* Handle cancel */ },
        colors = ButtonDefaults.buttonColors(
            Purple40
        ),
        modifier = Modifier
            .height(50.dp)
            .padding(end = 10.dp),
        shape = CircleShape
    ) {
        Text(text = "Cancel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}