package com.example.mealmates.ui.views

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.fragment.app.Fragment
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.mealmates.constants.GlobalObjects
import com.google.maps.android.compose.rememberMarkerState

class LocationSettingPage : AppCompatActivity() {
    val PERMISSION_REQUEST_CODE = 9
    private lateinit var context: Context
    private lateinit var placesClient: PlacesClient
    private lateinit var loginModel: LoginViewModel
    var currentPlacesFound by mutableStateOf(false)
    var enterLocationPrompt by mutableStateOf(false)
    var currentPlaceLatLng by mutableStateOf(LatLng(0.0, 0.0))
    @Composable
    fun LocationSettings(loginModel: LoginViewModel, placesClient: PlacesClient, onNavigateToMainPage: () -> Unit = {}) {
        this.placesClient = placesClient
        this.loginModel = loginModel
        this.context = LocalContext.current
        checkPermission()
        FindCurrentLocationButton(onNavigateToMainPage, currentPlacesFound)
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

    private fun checkPermissionThenFindCurrentPlace() {
        when {
            (ContextCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) -> {
                println("permission granted, find current place")
                findCurrentPlace()
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
    private fun findCurrentPlace() {
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
                    val currentUser = this.loginModel.user
                    if (mostLikelyPlaceLatLng != null) {
                        currentUser.location = mostLikelyPlaceLatLng
                        UserApi().updateUser(currentUser)
                        currentPlacesFound = true
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

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun FindCurrentLocationButton(
        onNavigateToMainPage: () -> Unit,
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
                color = md_theme_light_primary,
            )
            if (!enterLocationPrompt || locationFound) {
                println(locationFound)
                println(currentPlaceLatLng)
                Button(
                    onClick = {
                        if (!locationFound) {
                            checkPermissionThenFindCurrentPlace()
                        } else {
                            GlobalObjects.user.location = currentPlaceLatLng
                            UserApi().addUser(GlobalObjects.user)
                            onNavigateToMainPage()
                        } },
                    colors = ButtonDefaults.buttonColors(
                        md_theme_light_primary
                    ),
                    modifier = Modifier
                        .height(50.dp)
                        .padding(end = 10.dp),
                    shape = CircleShape
                ) {
                    var text = "Find Current Location"
                    if (locationFound) {
                        text = "Continue"
                    }
                    Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = { /* TODO let user enter location manually */ },
                    colors = ButtonDefaults.buttonColors(
                        md_theme_light_primary
                    ),
                    modifier = Modifier
                        .height(50.dp)
                        .padding(end = 10.dp),
                    shape = CircleShape
                ) {
                    val text = "Enter Current Location"
                    Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (currentPlaceLatLng != LatLng(0.0, 0.0)) {
                GoogleMap(
                    modifier = Modifier
                        .height((LocalConfiguration.current.screenHeightDp * 0.60).dp)
                        .width((LocalConfiguration.current.screenWidthDp * 0.90).dp),
                    cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(currentPlaceLatLng, 10f)
                    }
                ) {
                    if (currentPlacesFound) {
                        Marker(
                            state = MarkerState(currentPlaceLatLng),
                            title = "Your Location",
                            snippet = "Your location"
                        )
                    }
                }
            }
        }
    }

}
