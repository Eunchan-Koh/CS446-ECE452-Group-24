package com.example.mealmates.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.constants.RESTAURANT_TYPE_LABEL_LIST
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.theme.component_colour
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.theme.primary_text_colour
import com.example.mealmates.ui.viewModels.LoginViewModel

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PreferenceAndRestrictions(
    loginModel: LoginViewModel,
    onNavigateToMainPage: () -> Unit = {},
    onNavigateToLocationPage: () -> Unit,
    onNavigateToProfilePage: () -> Unit,
) {
    val selectedPreferences = remember { mutableStateListOf<String>() }

    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0)),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Food Preferences",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = primary_text_colour,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 30.dp)
            )

            InfoMessageCard()

            FlowRow(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (r in RESTAURANT_TYPE_LABEL_LIST) {
                    var selected by remember { mutableStateOf(false) }

                    FilterChip(
                        onClick = {
                            selected = !selected;
                            if (selected) {
                                selectedPreferences.add(r.value)
                            } else {
                                selectedPreferences.remove(r.value)
                            }
                        },
                        label = {
                            Text(r.key)
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

            SaveChangesButton(loginModel, onNavigateToMainPage, onNavigateToLocationPage, onNavigateToProfilePage, selectedPreferences);
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SaveChangesButton(loginModel: LoginViewModel, onNavigateToMainPage: () -> Unit = {}, onNavigateToLocationPage: () -> Unit = {}, onNavigateToProfilePage: () -> Unit = {}, selectedPreferences: List<String>) {
    val buttonColor = remember { mutableStateOf(md_theme_light_primary) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                val oldPreferences = GlobalObjects.user.preferences;
                GlobalObjects.user.preferences = selectedPreferences;
                GlobalObjects.user.restrictions = selectedPreferences;
                if(GlobalObjects.user.location.latitude.equals(0.0) && GlobalObjects.user.location.longitude.equals(0.0)) {
                    onNavigateToLocationPage();
                } else if(oldPreferences != GlobalObjects.user.preferences) {
                    onNavigateToProfilePage();
                } else {
                    onNavigateToMainPage();
                }
            },
            colors = ButtonDefaults.buttonColors(
                button_colour,
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

@Composable
fun InfoMessageCard() {
    Card(
        backgroundColor = component_colour,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row {
                Icon(
                    Icons.Outlined.Info,
                    "Important information",
                    tint = primary_text_colour,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                Text(
                    text = "Important Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = primary_text_colour
                )
            }

            Text(
                text = "Currently, our app does not provide specific information on Allergens and Religious dietary restrictions due to data limitations. Please verify dietary information directly with the restaurants. Thank you for understanding."
            )
        }
    }
}
