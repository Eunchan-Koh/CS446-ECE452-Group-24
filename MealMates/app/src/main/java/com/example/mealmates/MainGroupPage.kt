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
import com.example.mealmates.ui.theme.MealMatesTheme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.constants.GROUP_STATE
import kotlin.math.abs

@Composable
fun MainGroupPage(){
    var currentPage by remember { mutableIntStateOf(1) }
    fun changeCurrentPage(): () -> Unit = {
        currentPage = abs(currentPage - 1)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MainAction(GROUP_STATE.MATCH)   // should be dynamic later
        GroupHeader("Group 1")
        LinkshareAndMembers(changeCurrentPage(), currentPage)
        Divider(color = Color.Black, thickness = 1.dp)
        if (currentPage == 1) {
            Preferences()
        } else {
            GroupMembersPage()
        }
    }
}

@Composable
fun MainAction(groupState: GROUP_STATE){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .height((LocalConfiguration.current.screenHeightDp * 0.25).dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        // Start Match / Vote / Final Decision
        // depends on "state" of group
        var actionText = ""
        actionText = when (groupState) {
            GROUP_STATE.MATCH -> "MATCH"
            GROUP_STATE.VOTE -> "VOTE"
            GROUP_STATE.FINAL -> "View Final Restaurant"
        }
        Button(
            onClick = { /* todo */ },
            colors = ButtonDefaults.buttonColors(Color.LightGray, Color.LightGray, Color.Black, Color.White),
            modifier = Modifier
                .height(100.dp)
                .width((LocalConfiguration.current.screenWidthDp * 0.60).dp),
            shape = RectangleShape,
            border = BorderStroke(2.dp, Color.Red)
        ) {
            Text(text = actionText, fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GroupHeader(groupName: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .height((LocalConfiguration.current.screenHeightDp * 0.10).dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(groupName, color = Color.Black, fontSize = 32.sp)
    }
}

@Composable
fun LinkshareAndMembers(changePage: () -> Unit, currentPage: Int){
    //hardcoded for now, will need to retrieve based on group id from database later
    val linkshareValue = "mealmates.share/1230"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp * 0.12).dp)
            .background(color = Color.White)
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ){
        // Link Share copy
        Button(
            onClick = { /* todo */ },
            colors = ButtonDefaults.buttonColors(Color.LightGray, Color.LightGray, Color.Black, Color.White),
            modifier = Modifier
                .height(50.dp)
                .width(140.dp),
            shape = RectangleShape
        ) {
            Text(text = "Copy Link", fontSize = 16.sp, color = Color.Black)
        }
        // View Members / Actions
        Button(
            onClick = { changePage() },
            colors = ButtonDefaults.buttonColors(Color.LightGray, Color.LightGray, Color.Black, Color.White),
            modifier = Modifier
                .height(50.dp)
                .width(140.dp),
            shape = RectangleShape
        ) {
            var buttonText = ""
            buttonText = when (currentPage) {
                1 -> "Members"
                else -> "Actions"
            }
            Text(text = buttonText, fontSize = 16.sp, color = Color.Black)
        }
    }
}

@Composable
fun Preferences(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp * 0.50).dp)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Food Preferences
        Button(
            onClick = { /* todo */ },
            colors = ButtonDefaults.buttonColors(Color.LightGray, Color.LightGray, Color.Black, Color.White),
            modifier = Modifier
                .height(80.dp)
                .width((LocalConfiguration.current.screenWidthDp * 0.60).dp),
            shape = RectangleShape
        ) {
            Text(text = "Food Preferences", fontSize = 20.sp, color = Color.Black)
        }
        // Location Preferences
        Button(
            onClick = { /* todo */ },
            colors = ButtonDefaults.buttonColors(Color.LightGray, Color.LightGray, Color.Black, Color.White),
            modifier = Modifier
                .height(80.dp)
                .width((LocalConfiguration.current.screenWidthDp * 0.60).dp),
            shape = RectangleShape
        ) {
            Text(text = "Location Preferences", fontSize = 20.sp, color = Color.Black)
        }
    }
}


