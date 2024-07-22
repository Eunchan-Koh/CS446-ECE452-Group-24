package com.example.mealmates.ui.views

import TextInput
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.models.User
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.theme.md_theme_light_onPrimary
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.mealmates.models.GetPlaceDetailsResponse

@Composable
fun CreateNewGroupPage(loginModel: LoginViewModel, onNavigateToMainPage: () -> Unit = {}) {
    val userCur = UserApi().getUser(GlobalObjects.user.id!!)
    var tempGroupName by remember { mutableStateOf("") }
    var tempGroupLocation: String
    var tempGroupProfilePic by remember { mutableStateOf(byteArrayOf(0)) }
    var tempGroupPreferences: List<String>
    var tempGroupRestrictions: List<String>
    var tempGroupMembers: List<String>

    val placeResponse = getPlaceDetails("ChIJj61dQgK6j4AR4GeTYWZsKWw")
    val Resto = GetPlaceDetailsResponse(placeResponse).getMealMatesPlace()

    MealMatesTheme{
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
//                .background(color = Color.Gray)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
//                    color = MaterialTheme.colorScheme.background
        ) {
            // row with "cancel             save (only clickable when filled out info)
            // upload group image
            // enter new group name
            // edit/add group members
            CancelAndSaveRow(onNavigateToMainPage)
            ChooseGroupProfilePic()
            TextInput(
                label = "Enter a group name",
                placeholder = "My new group",
                value = tempGroupName,
                onValueChange = { tempGroupName = it })
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
fun CancelAndSaveRow(onNavigateToMainPage: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { onNavigateToMainPage() },
            colors = ButtonDefaults.buttonColors(containerColor = button_colour),
            modifier = Modifier.height(40.dp),
            shape = CircleShape
        ) {
            Text("Cancel")
        }
        Spacer(
            Modifier
                .weight(1f)
                .fillMaxHeight())
        Button(
            onClick = { onNavigateToMainPage() },
            colors = ButtonDefaults.buttonColors(containerColor = button_colour),
            modifier = Modifier.height(40.dp),
            shape = CircleShape
        ) {
            Text("Save")
        }
    }
}

@Composable
fun ChooseGroupProfilePic() {
    val curCon = LocalContext.current
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .clickable {
                Toast.makeText(curCon, "Clicked on Image Selection area!",
                    Toast.LENGTH_SHORT).show()
            }
    ) {
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
}

@Composable
fun InputNewGroupName() {

}
