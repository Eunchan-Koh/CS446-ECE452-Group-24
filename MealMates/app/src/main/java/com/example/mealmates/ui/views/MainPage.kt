package com.example.mealmates.ui.views

import android.graphics.BitmapFactory
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.R
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.constants.RESTAURANT_TYPE_LABEL_LIST
import com.example.mealmates.models.Group
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.theme.component_colour
import com.example.mealmates.ui.theme.primary_text_colour
import com.example.mealmates.ui.viewModels.LoginViewModel

@Composable
fun MainPage(
    loginModel: LoginViewModel,
    onNavigateToGroup: (Group) -> Unit = {},
    onNavigateToMatches: (Int) -> Unit = {},
    onNavigateToCreateNewGroup: () -> Unit = {}
) {
    val (TotalGroupNum, setGroupNum) = remember {
        mutableStateOf(2)
    }
    MealMatesTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AdvertisementSectionMainPage()
            ListGroupsMainPage(onNavigateToGroup, onNavigateToMatches)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ){
                FloatingActionButMainPage(onNavigateToCreateNewGroup)
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
            .background(color = component_colour),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Image(
            painter = painterResource(id = R.drawable.mc_image),
            contentDescription = "aa",
            contentScale = ContentScale.FillBounds)
//        Text("Advertisement Image Section")
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
fun ListGroupsMainPage(
    onNavigateToGroup: (Group) -> Unit = {},
    onNavigateToMatches: (Int) -> Unit = {}
) {
    val userId = GlobalObjects.user.id
    var groups by remember { mutableStateOf<List<Group>>(emptyList()) }

    LaunchedEffect(userId) {
        if (userId != null) {
            val fetchedGroups = UserApi().getUserGroups(userId)
            groups = fetchedGroups
        }
    }

    val groupSize = groups.size
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        for (i in 0 until groupSize) {
            Column(
                modifier = Modifier
                    .height(100.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .background(color = component_colour, RoundedCornerShape(20.dp))
                    .clickable { onNavigateToGroup(groups[i]) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var groupImage = BitmapFactory.decodeByteArray(groups[i].image, 0, groups[i].image.size)
                        if (groupImage != null) {
                            Image(
                                bitmap = groupImage.asImageBitmap(),
                                contentDescription = "",
                                modifier = Modifier.size(82.dp)
                            )
                        } else {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "Group Image",
                                tint = Color.LightGray,
                                modifier = Modifier.size(82.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 10.dp, horizontal = 5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            var groupName = groups[i].name
                            Text(groupName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color= primary_text_colour)
                            Spacer(modifier = Modifier.width(40.dp))
                            Spacer(modifier = Modifier.height(2.dp))
                            var tagNames: String
                            var tempSum = 0
                            Row {
                                Text(text = "Tags: ", color = primary_text_colour)
                                for (a in groups[i].preferences.indices) {
                                    val formattedPreferences =
                                        groups[i].preferences.map { preference ->
                                            RESTAURANT_TYPE_LABEL_LIST.entries.find { it.value == preference }?.key ?: preference
                                        }
                                    tagNames = formattedPreferences[a]
                                    tempSum += groups[i].preferences[0].length
                                    if (tempSum <= 40) {
                                        if (a == 0) {
                                            Text(text = tagNames, fontSize = 18.sp, color = primary_text_colour)
                                        } else {
                                            Text(text = ", $tagNames", fontSize = 18.sp, color = primary_text_colour)
                                        }
                                    } else {
                                        Text(text = ",...", color = primary_text_colour)
                                        break
                                    }
                                }
                            }
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "View Matches",
                        tint = button_colour,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onNavigateToMatches(groups[i].gid) }
                    )
                }
            }
            Divider(color = Color.White, thickness = 1.dp)
            Spacer(
                modifier = Modifier
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