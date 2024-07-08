package com.example.mealmates.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.theme.selectableList_colour
import com.example.mealmates.ui.viewModels.LoginViewModel

@Composable
fun MainPage(loginModel: LoginViewModel, onNavigateToRestaurantPrompts: () -> Unit = {}, OnNavigateToGroup: () -> Unit = {}){

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
            ListGroupsMainPage(TotalGroupNum, OnNavigateToGroup)

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
            BottomMenu(onNavigateToRestaurantPrompts = onNavigateToRestaurantPrompts)
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
fun ListGroupsMainPage(GroupNum: Int, OnNavigateToGroup: () -> Unit = {}){
    for(i in 1..GroupNum){
        Column(
            modifier = Modifier
                .height(82.dp)
                .padding(horizontal = 0.dp, vertical = 0.dp)
                .fillMaxWidth()
                .clickable { OnNavigateToGroup() }
                .background(color = selectableList_colour),

            ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
//                Image(painter = , contentDescription = )
                Icon(
                    Icons.Default.AddCircle,
                    "add",
                    tint = Color.LightGray,
                    modifier = Modifier
                        .padding(10.dp)
                        .width(82.dp)
                        .height(82.dp),
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Group$i", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(40.dp))
                    Text("Tags: pizza, hamburger, ...")
                }
            }
        }
        Divider(color = Color.White, thickness = 1.dp)
    }
}

@Composable
fun BottomMenu(onNavigateToRestaurantPrompts: () -> Unit = {}, onNavigateToMainPage: () -> Unit = {}){
    Column(
        modifier = Modifier
            .height(50.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ){
            Button(
                onClick = { onNavigateToMainPage() },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(),

                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Home",color = Color.Black)
            }
            Button(
                onClick = { onNavigateToRestaurantPrompts() },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Match",color = Color.Black)
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
        containerColor = button_colour,
        shape = CircleShape


    ) {
        Icon(Icons.Default.Add, "add", tint = Color.White)
    }
}