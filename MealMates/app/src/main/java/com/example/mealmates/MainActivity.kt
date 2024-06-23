package com.example.mealmates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mealmates.ui.RestaurantInfo
import com.example.mealmates.ui.RestaurantProfile
import com.example.mealmates.ui.theme.MealMatesTheme

val RESTAURANT_DATA =
    listOf(
        RestaurantInfo(
            name = "Gol's Lanzou Noodle",
            address = "150 University Ave W Unit 6B, Waterloo, ON N2L 3E4",
            website = "http://www.lanzhou.ca/",
            photos =
                listOf(
                    "https://lh5.googleusercontent.com/p/AF1QipMi2Wy7AWGgVy_CtZJiSBFr7V2iLm28BEjNcjMJ=w101-h126-n-k-no-nu",
                    "https://lh5.googleusercontent.com/p/AF1QipO-8GaKX8BhafiWtS9zf2cvXO-L91sB_4_2UYEI=w101-h168-n-k-no-nu",
                    "https://lh5.googleusercontent.com/p/AF1QipMuJQaNzk8Pby5URLp-ctUh1-0R7Q1cGeePCBlW=w141-h235-n-k-no-nu")),
        RestaurantInfo(
            name = "Lazeez Shawarma",
            address = "170 University Ave W, Waterloo, ON N2L 3E9",
            website = "https://www.lazeezshawarma.com/",
            photos =
                listOf(
                    "https://lh5.googleusercontent.com/p/AF1QipO9BYoECm621og8GC0wxggq87gG2JOSm20FI5P5=w141-h118-n-k-no-nu",
                    "https://lh5.googleusercontent.com/p/AF1QipNm392wqqVvODh2iNPCEXvdPVSzq0MejvX5LBCt=w141-h235-n-k-no-nu",
                )),
        RestaurantInfo(
            name = "Campus Pizza",
            address = "160 University Ave W #2, Waterloo, ON N2L 3E9",
            website = "http://www.campuspizza.ca/",
            photos =
                listOf(
                    "https://lh5.googleusercontent.com/p/AF1QipP7fDcPKBo7OfwtcEx4Qmr4uWurq7e_1D2Xvh-a=w141-h118-n-k-no-nu",
                    "https://lh5.googleusercontent.com/p/AF1QipOAGYi-SDADzNkfOCDKNa-5JXCUYvVzlAPV-y1O=w141-h176-n-k-no-nu",
                )),
    )

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealMatesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                        RestaurantProfile(info = RESTAURANT_DATA[0], tags = listOf())
                    }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MealMatesTheme { Greeting("Android") }
}
