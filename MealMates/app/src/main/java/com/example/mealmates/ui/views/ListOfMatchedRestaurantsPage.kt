package com.example.mealmates.ui.views

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.example.mealmates.constants.RESTAURANT_DATA

@Composable
fun ListOfMatchedRestaurantsPage(loginModel: LoginViewModel, onNavigateToMainPage: () -> Unit) {

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
            ListRestaurantsListOfMatched(2)//using constant for demo - will be
            //receiving proper var in further development


        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomMenu({}, onNavigateToMainPage)
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
        AsyncImage(
            model = RESTAURANT_DATA[2].photos[0],
            contentDescription = "Image for top rated restaurant",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize())//hard coded for now
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
        Text(RESTAURANT_DATA[2].name)//hardcoded for now
        Spacer(modifier = Modifier.padding(10.dp))
        Text(RESTAURANT_DATA[2].address)
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
    for(i in 0..GroupNum-1){
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
                AsyncImage(model = RESTAURANT_DATA[i].photos[0],
                    contentDescription = "restaurant image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(75.dp)
                        .clip(CircleShape)
                )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 0.dp, horizontal = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(RESTAURANT_DATA[i].name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(RESTAURANT_DATA[i].address, fontSize = 15.sp)
                }
            }


        }
        Divider(color = Color.White, thickness = 1.dp)
    }
}
