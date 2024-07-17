package com.example.mealmates.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.viewModels.LoginViewModel

@Composable
fun UserProfileManagementPage(loginModel: LoginViewModel) {

    val (TotalGroupNum, setGroupNum) = remember {
        mutableStateOf(2)
    }
    MealMatesTheme{
        Column(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.92f)
//                .background(color = Color.Gray)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
//                    color = MaterialTheme.colorScheme.background
        ) {
            Spacer(modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
//                .background(color = Color.Green),
                )
            choosePicProfile()
            NameSetupProfile()
            LocationSetupProfile()
            Spacer(modifier = Modifier
                .height(180.dp)
//                .background(color = Color.Gray)
                .fillMaxWidth())
            EditPrefProfile()
            SaveButtonProfile()

        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
        }

    }
}


@Composable
fun choosePicProfile(){
    Icon(
        Icons.Default.AddCircle,
        "add",
        tint = Color.LightGray,
        modifier = Modifier
            .padding(10.dp)
//            .width(82.dp)
//            .height(82.dp)
            .size(110.dp),
    )
}

@Composable
fun NameSetupProfile(){
    val (value, setValue) = remember {
        mutableStateOf("")
    }
    Column(//Search Section
        modifier = Modifier
            .height(92.dp)
            .fillMaxWidth()
//            .background(color = Color.Green)
            .padding(0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        TextField(
            value = value,
            onValueChange = setValue,
            modifier = Modifier
                .width(150.dp)
                .padding(10.dp),
            placeholder = {
                Text("John Doe", color = Color.Gray, textAlign = TextAlign.Center)
            }
        )
    }
}
@Composable
fun LocationSetupProfile() {
    val (value, setValue) = remember {
        mutableStateOf("")
    }
    Column(//Search Section
        modifier = Modifier
            .height(92.dp)
            .fillMaxWidth()
//            .background(color = Color.Green)
            .padding(horizontal = 30.dp, vertical = 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TextField(
            value = value,
            onValueChange = setValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
                Text("Location for search", color = Color.Gray)
            }
        )
    }
}

@Composable
fun EditPrefProfile(){
    Column(
        modifier = Modifier.height(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClickableText(
            AnnotatedString("Edit preferences & restrictions"),
            onClick = {/*link to preference & restrictions page here*/}
        )
    }
}

@Composable
fun SaveButtonProfile(){
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ){
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = button_colour)
            ){
            Text("Save")
        }
    }

}