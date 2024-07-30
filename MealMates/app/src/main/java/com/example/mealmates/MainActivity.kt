package com.example.mealmates

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.example.mealmates.ui.views.Login
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel: LoginViewModel by viewModels()
        mainViewModel.setContext(this)
        Places.initialize(this, BuildConfig.PLACES_API_KEY)
        val placesClient = Places.createClient(this)
        setContent {
            Login(mainViewModel, placesClient)
        }
    }
}