package com.example.mealmates

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.ui.theme.MealMatesTheme
import kotlinx.coroutines.launch
@Composable
fun ListOfMatchedRestaurantsPage(){

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