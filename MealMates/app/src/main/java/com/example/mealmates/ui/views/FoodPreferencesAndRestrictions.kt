package com.example.mealmates.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.viewModels.LoginViewModel


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PreferenceAndRestrictions(loginModel: LoginViewModel, onNavigateToMainPage: () -> Unit = {}) {
    val preferences = listOf(
        "Chinese", "Indian", "Italian", "Korean",
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
                .padding(30.dp)
        ) {
            Text(
                text = "Food Preferences",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_primary,
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
                color = md_theme_light_primary,
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
        SaveChangesButton(loginModel, onNavigateToMainPage);
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SaveChangesButton(loginModel: LoginViewModel, onNavigateToMainPage: () -> Unit = {}) {
    val buttonColor = remember { mutableStateOf(md_theme_light_primary) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { onNavigateToMainPage() },
            colors = ButtonDefaults.buttonColors(
                md_theme_light_primary
            ),
            modifier = Modifier
                .height(50.dp),
            shape = CircleShape
        ) {
            Text(text = "Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CancelButton() {
    Button(
        onClick = { /* Handle cancel */ },
        colors = ButtonDefaults.buttonColors(
            md_theme_light_primary
        ),
        modifier = Modifier
            .height(50.dp)
            .padding(end = 10.dp),
        shape = CircleShape
    ) {
        Text(text = "Cancel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}