package com.example.mealmates.ui.views

import android.graphics.BitmapFactory
import android.graphics.Paint.Align
import android.widget.Space
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.models.Group
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.theme.selectableList_colour
import com.example.mealmates.ui.viewModels.LoginViewModel

@Composable
fun MainPage(loginModel: LoginViewModel, onNavigateToRestaurantPrompts: () -> Unit = {}, OnNavigateToGroup: () -> Unit = {}, OnNavigateToCreateNewGroup: () -> Unit = {}){

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
//            SearchBarSectionMainPage()
//            Divider(color = Color.Black, thickness = 1.dp)
            ListGroupsMainPage(OnNavigateToGroup)

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
                FloatingActionButMainPage(OnNavigateToCreateNewGroup)
            }
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
fun ListGroupsMainPage(OnNavigateToGroup: () -> Unit = {}) {
    //TODO: Use this information of all the user's groups. Left to Eunchan.

    val userId = GlobalObjects.user.id
    var groups by remember { mutableStateOf<List<Group>>(emptyList()) }

    if (userId != null) {
        val fetchedGroups = UserApi().getUserGroups(userId)
        groups = fetchedGroups
    }

    val groupSize = groups.size
//    Text("$temp1s")
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {

        for (i in 0..groupSize-1) {
            Column(
                modifier = Modifier
                    .height(92.dp)
                    .padding(horizontal = 0.dp, vertical = 0.dp)
                    .fillMaxWidth()
                    .clickable { OnNavigateToGroup() }
                    .background(color = selectableList_colour),
//                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
//                Image(painter = , contentDescription = )
                    var GroupNImage = BitmapFactory.decodeByteArray(groups[i].image,
                        0, groups[i].image.size)
//                    Text(text = "$tempI")
                    if (GroupNImage!=null)
                        Image(bitmap = GroupNImage.asImageBitmap(), contentDescription = "")
                    else Icon(
                        Icons.Default.AddCircle,
                        "add",
                        tint = Color.LightGray,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .width(82.dp)
                            .height(82.dp),
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = 10.dp, horizontal = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        var GroupNName = groups[i].name
                        Text(GroupNName, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(40.dp))
                        var tagNames : String
                        var tempSum = 0
                        Row (

                        ){
                            Text(text = "tags: ")
                            for(a in 0..<groups[i].preferences.size){
                                tagNames = groups[i].preferences[a]
                                tempSum += groups[i].preferences[0].length
                                if( tempSum <= 20)
                                    Text(text = "$tagNames,", fontSize = 18.sp)
                                else {
                                    Text(text = "...")
                                    break
                                }
                            }
                        }

//                        Text("Tags: pizza, hamburger, ...", fontSize = 18.sp)
                    }
                }
            }
            Divider(color = Color.White, thickness = 1.dp)
            Spacer(modifier = Modifier
                .height(5.dp)
                .fillMaxWidth()
                .background(color = Color.White)
            )
        }
    }

}


@Composable
fun FloatingActionButMainPage(OnNavigateToCreateNewGroup: () -> Unit = {}){
    FloatingActionButton(onClick = { OnNavigateToCreateNewGroup() },
        modifier = Modifier
            .padding(30.dp)
            .size(50.dp),
        containerColor = button_colour,
        shape = CircleShape


    ) {
        Icon(Icons.Default.Add, "add", tint = Color.White)
    }
}