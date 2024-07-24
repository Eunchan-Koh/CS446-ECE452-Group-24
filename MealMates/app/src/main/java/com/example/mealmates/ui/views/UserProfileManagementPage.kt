package com.example.mealmates.ui.views

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.constants.RESTAURANT_TYPE_LABEL_LIST
import com.example.mealmates.models.User
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.theme.component_colour
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.theme.primary_text_colour
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun UserProfileManagementPage(
    loginModel: LoginViewModel,
    onNavigateToSurvey: () -> Unit = {},
    onNavigateToLocationPage: () -> Unit = {}
) {
    val currentUser = GlobalObjects.user
    val userName = currentUser.name
    val userLocation = currentUser.location
    var tempUserName = userName
    var tempUserLocation: String

    MealMatesTheme {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .background(
                                brush =
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFffd7f6), Color.White)))) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 20.dp).verticalScroll(
                                ScrollState(0)
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "User Settings",
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = primary_text_colour,
                                )
                                ChoosePicProfile(currentUser)
                            }
                    }
                Spacer(modifier = Modifier.height(8.dp))
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                    Column(
                        modifier =
                            Modifier.fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    color = component_colour,
                                    shape =
                                        RoundedCornerShape(
                                            topStart = 30.dp,
                                            topEnd = 30.dp,
                                            bottomStart = 30.dp,
                                            bottomEnd = 30.dp))
                                .padding(
                                    horizontal = 20.dp,
                                    vertical = 8.dp) // Add padding inside the column
                        ) {
                            tempUserName = nameSetupProfile(userName)

                            // Display email
                            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                Text(
                                    text = "Email",
                                    color = primary_text_colour,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                )
                                Text(
                                    text = currentUser.email, fontSize = 16.sp, color = primary_text_colour)
                            }
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 4.dp))
                            Preferences(currentUser.preferences, onNavigateToSurvey)
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 4.dp))
                            // Location is being shown using point only; need to be fixed to address
                            // later
                            LocationSetupProfile(userLocation, onNavigateToLocationPage)
                        }
                }
                SaveProfileButton(currentUser, tempUserName)
            }
    }
}

@Composable
fun ChoosePicProfile(currentUser: User) {
    val curCon = LocalContext.current

    Box(
        contentAlignment = Alignment.TopEnd,
        modifier =
            Modifier.clickable {
                Toast.makeText(curCon, "Clicked on Image Selection area!", Toast.LENGTH_SHORT)
                    .show()
            }) {
            val userImage =
                BitmapFactory.decodeByteArray(currentUser.image, 0, currentUser.image.size)
            if (userImage != null)
                Image(bitmap = userImage.asImageBitmap(), contentDescription = "")
            else
                Icon(
                    Icons.Default.AccountCircle,
                    "add",
                    tint = primary_text_colour,
                    modifier = Modifier.padding(10.dp).size(110.dp),
                )
//            Icon(
//                Icons.Default.Edit,
//                "Change profile photo",
//                tint = button_colour,
//                modifier = Modifier.padding(6.dp))
        }
}

@Composable
fun nameSetupProfile(userName: String): String {
    val (value, setValue) = remember { mutableStateOf(userName) }
    Column(modifier = Modifier.height(92.dp).fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = "Name",
            color = primary_text_colour,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        OutlinedTextField(
            value = value,
            onValueChange = setValue,
            modifier = Modifier.fillMaxWidth(),
            colors =
                OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = primary_text_colour,
                    unfocusedLeadingIconColor = Color.White),
            singleLine = true,
            placeholder = { Text("Name", color = Color.Gray, textAlign = TextAlign.Center) })
    }
    return value
}

@Composable
fun LocationSetupProfile(userLocation: LatLng, onNavigateToLocationPage: () -> Unit = {}) {
    val (value, setValue) = remember { mutableStateOf(userLocation.toString()) }
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Location",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = primary_text_colour,
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit location",
                    tint = button_colour,
                    modifier = Modifier.size(24.dp).clickable { onNavigateToLocationPage() })
            }
        GoogleMap(
            modifier =
                Modifier.height((LocalConfiguration.current.screenHeightDp * 0.28).dp)
                    .width((LocalConfiguration.current.screenWidthDp * 0.90).dp),
            cameraPositionState =
                rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(userLocation, 10f)
                })
    }
    // TODO: Add ability to search for new location

    //    Column(//Search Section
    //        modifier = Modifier
    //            .height(92.dp)
    //            .fillMaxWidth()
    ////            .background(color = Color.Green)
    //            .padding(horizontal = 30.dp, vertical = 0.dp),
    //        verticalArrangement = Arrangement.Center,
    //        horizontalAlignment = Alignment.CenterHorizontally
    //
    //    ) {
    //        TextField(
    //            value = value,
    //            onValueChange = setValue,
    //            modifier = Modifier
    //                .fillMaxWidth()
    //                .padding(10.dp),
    //            placeholder = {
    //                Text("Location for search", color = Color.Gray)
    //            }
    //        )
    //    }
    //    return value
}

@Composable
fun Preferences(preferences: List<String>, onNavigateToSurvey: () -> Unit = {}) {
    val formattedPreferences =
        preferences.map { preference ->
            RESTAURANT_TYPE_LABEL_LIST.entries.find { it.value == preference }?.key ?: preference
        }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Preferences",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = primary_text_colour,
                )
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit preferences",
                    tint = button_colour,
                    modifier = Modifier.size(24.dp).clickable { onNavigateToSurvey() })
            }
        Text(
            text = formattedPreferences.joinToString(", "),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
        )
    }
}

// TODO: Currently only saves name changes. Make it work for profile picture and email
fun onSave(currentUser: User, newName: String) {
    currentUser.name = newName
    UserApi().updateUser(currentUser)
}

@Composable
fun SaveProfileButton(currentUser: User, tempUserName: String) {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .height(50.dp),
        onClick = {
            onSave(currentUser, tempUserName)
            Toast.makeText(context, "Saved Profile Changes", Toast.LENGTH_SHORT).show()
        },
        colors = ButtonDefaults.buttonColors(containerColor = button_colour)) {
            Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
}
