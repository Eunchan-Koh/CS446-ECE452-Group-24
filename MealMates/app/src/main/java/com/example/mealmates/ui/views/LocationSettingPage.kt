package com.example.mealmates.ui.views

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import androidx.compose.ui.graphics.RectangleShape
import com.example.mealmates.apiCalls.GroupApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.models.AutocompletePlace
import com.example.mealmates.models.Group
import com.example.mealmates.models.LocationAutocompleteResponse
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.theme.component_colour
import com.example.mealmates.ui.theme.on_button_colour
import com.example.mealmates.ui.theme.primary_text_colour
import com.google.maps.android.compose.CameraPositionState

class LocationSettingPage : AppCompatActivity() {
    val PERMISSION_REQUEST_CODE = 9
    private lateinit var context: Context
    private lateinit var placesClient: PlacesClient
    private lateinit var loginModel: LoginViewModel
    var currentPlaceFound by mutableStateOf(false)
    var enterLocationPrompt by mutableStateOf(false)
    var currentPlaceLatLng by mutableStateOf(LatLng(0.0, 0.0))
    @Composable
    fun LocationSettings(
        loginModel: LoginViewModel,
        placesClient: PlacesClient,
        isFromGroupPage: Boolean,
        onNavigateToMainOrProfilePage: () -> Unit = {},
        onNavigateToGroupPage: (Group) -> Unit,
        groupId: String?,
        group: Group?
    ) {
        this.placesClient = placesClient
        this.loginModel = loginModel
        this.context = LocalContext.current
        println("aaaaaa")
        if (group != null) {
            println(group.uids)
            println(group.name)
        }
        val curGroup = if (groupId != null) GroupApi().getGroup(groupId) else group
        checkPermission()
        FindCurrentLocationButton(isFromGroupPage, onNavigateToMainOrProfilePage, onNavigateToGroupPage, groupId, curGroup, currentPlaceFound)
    }

    private fun checkPermission() {
        when {
            (ContextCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) -> {
                println("permission already granted, no need to check log")
            }

            else -> {
                // Ask for both the ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions.
                println("ask for permission log")
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                    ),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun checkPermissionThenFindCurrentPlace(
        isFromGroupPage: Boolean,
        onNavigateToMainOrProfilePage: () -> Unit,
        onNavigateToGroupPage: (Group) -> Unit
    ) {
        when {
            (ContextCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) -> {
                println("permission granted, find current place")
                findCurrentPlace(isFromGroupPage, onNavigateToMainOrProfilePage, onNavigateToGroupPage)
            }

            else -> {
                println("permission wasn't given")
                // TODO prompt user to enter address?
                enterLocationPrompt = true
            }
        }
    }

    /**
     * Fetches a list of [PlaceLikelihood] instances that represent the Places the user is
     * most likely to be at currently.
     */
    @RequiresPermission(anyOf = [ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION])
    private fun findCurrentPlace(
        isFromGroupPage: Boolean,
        onNavigateToMainOrProfilePage: () -> Unit,
        onNavigateToGroupPage: (Group) -> Unit
    ) {
        val placeFields: List<Place.Field> =
            listOf(Place.Field.NAME, Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // Retrieve likely places based on the device's current location
            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods ?: emptyList()) {
                        Log.i(
                            TAG,
                            "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}"
                        )
                    }
                    val mostLikelyPlaceLatLng = response.placeLikelihoods[0].place.latLng
                    val currentUser = GlobalObjects.user
                    if (mostLikelyPlaceLatLng != null) {
                        currentUser.location = mostLikelyPlaceLatLng
                        if(!mostLikelyPlaceLatLng.equals(GlobalObjects.user.location)) {
                            GlobalObjects.user.location = mostLikelyPlaceLatLng
                        }
                        currentPlaceFound = true
                        currentPlaceLatLng = mostLikelyPlaceLatLng
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e(TAG, "Place not found: ${exception.statusCode}")
                    }
                }
            }
        } else {
            Log.d(TAG, "LOCATION permission not granted")
        }
    }

    @Composable
    fun FindCurrentLocationButton(
        isFromGroupPage: Boolean,
        onNavigateToMainOrProfilePage: () -> Unit,
        onNavigateToGroupPage: (Group) -> Unit,
        groupId: String?,
        group: Group?,
        locationFound: Boolean
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set up your location",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = primary_text_colour,
            )
            if (!enterLocationPrompt || locationFound) {
                Button(
                    onClick = {
                        if (!locationFound) {
                            checkPermissionThenFindCurrentPlace(isFromGroupPage, onNavigateToMainOrProfilePage, onNavigateToGroupPage)
                        } else {
                            if (isFromGroupPage && group != null ) {
                                group.location = currentPlaceLatLng
                                println("?????")
                                println(group.uids)
                                onNavigateToGroupPage(group)
                            } else {
                                GlobalObjects.user.location = currentPlaceLatLng
                                UserApi().addUser(GlobalObjects.user)
                                println("Navigating to main or profile")
                                onNavigateToMainOrProfilePage()
                            }
                        } },
                    colors = ButtonDefaults.buttonColors(
                        button_colour
                    ),
                    modifier = Modifier
                        .height(50.dp)
                        .padding(end = 10.dp),
                    shape = CircleShape
                ) {
                    val text = if (!locationFound) "Find Current Location" else "Save"
                    Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = on_button_colour)
                }
            }
            if (enterLocationPrompt) {
                LocationSearchBar()
            }
            if (currentPlaceLatLng != LatLng(0.0, 0.0)) {
                ShowGoogleMap(currentPlaceLatLng)
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun ShowGoogleMap(currentPlaceLatLng: LatLng) {
        GoogleMap(
            modifier = Modifier
                .height((LocalConfiguration.current.screenHeightDp * 0.60).dp)
                .width((LocalConfiguration.current.screenWidthDp * 0.90).dp),
            cameraPositionState = CameraPositionState(CameraPosition(currentPlaceLatLng, 10f, 0f, 0f))
        ) {
            if (currentPlaceFound) {
                Marker(
                    state = MarkerState(currentPlaceLatLng),
                    title = "Your Location",
                    snippet = "Your location"
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LocationSearchBar() {
        val context = LocalContext.current
        var expanded by remember { mutableStateOf(false) }
        var selectedLocation by remember { mutableStateOf("") }
        var selectedLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
        var textInput by remember { mutableStateOf("") }
        var currentAutocompleteLocations by remember { mutableStateOf(listOf<AutocompletePlace>()) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {}
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth().height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    val displayText = if (selectedLocation != "") selectedLocation else textInput
                    OutlinedTextField(
                        value = displayText,
                        onValueChange = {
                            selectedLocation = ""
                            selectedLatLng = LatLng(0.0, 0.0)
                            textInput = it
                        },
                        modifier = Modifier.menuAnchor().weight(1f).height(60.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedLabelColor = md_theme_light_primary,
                            unfocusedLeadingIconColor = Color.White
                        ),
                        singleLine = true,
                    )
                    Button(
                        onClick = {
                            expanded = !expanded
                            if (expanded) {
                                val locationAutocompleteResponse = locationAutocomplete(textInput)
                                val predictionList = LocationAutocompleteResponse(locationAutocompleteResponse).getAutocompletePlaces()
                                currentAutocompleteLocations = predictionList
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = button_colour),
                        modifier = Modifier.fillMaxHeight().width(60.dp),
                        shape = RectangleShape,
                        content = {
                            Icon (
                                imageVector = Icons.Filled.Search,
                                contentDescription = "search",
                                tint = component_colour,
                                modifier = Modifier.fillMaxSize()
                            )
                        },
                        contentPadding = PaddingValues(8.dp)
                    )
                }
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    currentAutocompleteLocations.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item.description) },
                            onClick = {
                                selectedLocation = item.description
                                selectedLatLng = item.location
                                currentPlaceLatLng = selectedLatLng
                                currentPlaceFound = true
                                expanded = false
                                Toast.makeText(context, item.description, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }

}
