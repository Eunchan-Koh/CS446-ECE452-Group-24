package com.example.mealmates.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.viewModels.LoginViewModel

@Composable
fun ListOfMatchedRestaurantsPage(loginModel: LoginViewModel) {

    MealMatesTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
//                    Image
            ImageSectionListOfMatched()
            Divider(color = Color.Black, thickness = 1.dp)
            TopRestaurantInfoSectionListOfMatched()
            //Others section
            OtherTopPicksSectionListOfMatched()
            ListRestaurantsListOfMatched(3)//using constant for demo - will be
            //receiving proper var in further development


        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomMenu()
        }

    }

}


@Composable
fun ImageSectionListOfMatched(){
    Column(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(color = Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Image of top restaurant match")
    }
}

@Composable
fun TopRestaurantInfoSectionListOfMatched(){
    Column(
        modifier = Modifier
            .height(99.dp)
            .fillMaxWidth()
            .background(color = Color.LightGray)
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ){
        Text("Restaurant name")
        Spacer(modifier = Modifier.padding(10.dp))
        Text("Restaurant details (location, hours, phone, etc.")
    }
}

@Composable
fun OtherTopPicksSectionListOfMatched(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ){
        Text("Other top picks", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun ListRestaurantsListOfMatched(GroupNum: Int){
    for(i in 1..GroupNum){
        Column(
            modifier = Modifier
                .height(82.dp)
                .padding(horizontal = 10.dp, vertical = 0.dp)
                .fillMaxWidth()
                .clickable { /*to the corresponding group page*/ },

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
//                Image(painter = , contentDescription = )
                Icon(Icons.Default.CheckCircle,
                    "add",
                    tint = Color.LightGray,
                    modifier = Modifier
                        .padding(10.dp)
                        .width(82.dp)
                        .height(82.dp)
                )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ){
                    Text("Restaurant$i", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Location")
                }
            }


        }
        Divider(color = Color.White, thickness = 1.dp)
    }
}