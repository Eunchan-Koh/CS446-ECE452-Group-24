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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
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
fun MainPage(){

    val (TotalGroupNum, setGroupNum) = remember {
        mutableStateOf(2)
    }
    MealMatesTheme{
        Column(
            modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
        ) {
            AdvertisementSectionMainPage()
            Divider(color = Color.Black, thickness = 1.dp)
            SearchBarSectionMainPage()
            Divider(color = Color.Black, thickness = 1.dp)
            ListGroupsMainPage(TotalGroupNum)

        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ){
                floatingActionButMainPage()
            }
            BottomMenu()
        }

    }
}


@Composable
fun AdvertisementSectionMainPage(){
    Column(//Advertisement Images Section
        modifier = Modifier
            .height(168.dp)
            .fillMaxWidth()
            .background(color = Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text("Advertisement Image Section")
    }
}

@Composable
fun SearchBarSectionMainPage(){
    val (value, setValue) = remember {
        mutableStateOf("")
    }
    Column(//Search Section
        modifier = Modifier
            .height(112.dp)
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        TextField(
            value = value,
            onValueChange = setValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
                Text("Search Group By Name...", color = Color.Gray)
            }
        )
    }
}

@Composable
fun ListGroupsMainPage(GroupNum: Int){
    for(i in 1..GroupNum){
        Column(
            modifier = Modifier
                .height(82.dp)
                .padding(horizontal = 10.dp, vertical = 0.dp)
                .fillMaxWidth()
                .clickable { /*to the corresponding group page*/},

            ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
//                Image(painter = , contentDescription = )
                Icon(Icons.Default.AddCircle,
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
                    Text("Group$i", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Tags: pizza, hamburger, ...")
                }
            }


        }
        Divider(color = Color.White, thickness = 1.dp)
    }
}

@Composable
fun BottomMenu(){
    Column(
        modifier = Modifier
            .height(50.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ){
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(),

                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Home",color = Color.Black)
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Profile",color = Color.Black)
            }
        }
    }

}


@Composable
fun floatingActionButMainPage(){
    FloatingActionButton(onClick = { /*open up create group page*/ },
        modifier = Modifier
            .padding(30.dp)
            .size(50.dp),
        shape = CircleShape

    ) {
        Icon(Icons.Default.Add, "add", tint = Color.White)
    }
}