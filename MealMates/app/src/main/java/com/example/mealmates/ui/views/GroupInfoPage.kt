package com.example.mealmates.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mealmates.R
import com.example.mealmates.apiCalls.GroupApi
import com.example.mealmates.apiCalls.RestaurantsApi
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.constants.RESTAURANT_TYPE_LABEL_LIST
import com.example.mealmates.models.Group
import com.example.mealmates.models.Matched
import com.example.mealmates.models.Restaurants
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

data class GroupMember(val name: String, val isAdmin: Boolean, val uid: String)

data class GroupInfo(
    val imageUrl: String?,
    val groupName: String,
    val members: List<GroupMember>,
    val preferences: String,
    val restrictions: String,
    val location: LatLng = LatLng(0.0, 0.0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInfoPage(
    loginModel: LoginViewModel,
    gid: Int,
    name: String,
    preferences: List<String>,
    restrictions: List<String>,
    uids: List<String>,
    image: ByteArray,
    location: LatLng,
    onNavigateToGroupSettings: (Group) -> Unit,
    onNavigateToRestaurantPrompts: () -> Unit,
    onNavigateToMatchedRestaurants: () -> Unit
) {
    val users = mutableListOf<GroupMember>()
    for (i in uids.indices) {
        val user = UserApi().getUser(uids[i])
        users.add(GroupMember(user.name, i == 0, uids[i]))
    }
    // Format preferences so that it shows nicely using maps from constants.
    val formattedPreferences =
        preferences.map { preference ->
            RESTAURANT_TYPE_LABEL_LIST.entries.find { it.value == preference }?.key ?: preference
        }
    val formattedRestrictions =
        restrictions.map { restriction ->
            RESTAURANT_TYPE_LABEL_LIST.entries.find { it.value == restriction }?.key ?: restriction
        }
    val groupInfo =
        GroupInfo(
            imageUrl = null,
            groupName = name,
            members = users,
            preferences = formattedPreferences.joinToString(", "),
            restrictions = formattedRestrictions.joinToString(", "),
            location = location)

    val group = GroupApi().getGroup(gid.toString())

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        HeaderSection(
            groupInfo,
            group,
            { onNavigateToGroupSettings(group) },
            { onNavigateToRestaurantPrompts() },
            { onNavigateToMatchedRestaurants() })
        ContentSection(groupInfo)
    }
}

fun userHasCompletedSwiping(matchedInfo: Matched): Boolean {
    val currentUserId = GlobalObjects.user.id
    return matchedInfo.completed.contains(currentUserId)
}

fun userCanViewPrompts(matchedInfo: Matched, groupInfo: GroupInfo): Boolean {
    val currentUserId = GlobalObjects.user.id
    val user = groupInfo.members.find { it.uid == currentUserId }
    if (userHasCompletedSwiping(matchedInfo)) {
        return false
    }
    return user?.isAdmin == true || matchedInfo.completed.isNotEmpty()
}

fun isMatchCompleted(matchedInfo: Matched, groupInfo: GroupInfo): Boolean {
    return matchedInfo.completed.size == groupInfo.members.size
}

@Composable
fun ActionButton(
    gid: Int,
    groupInfo: GroupInfo,
    onNavigateToRestaurantPrompts: () -> Unit,
    onNavigateToMatchedRestaurants: () -> Unit,
) {
    var matchedInfo by remember { mutableStateOf(Matched()) }

    LaunchedEffect(Unit) {
        val res: Restaurants = RestaurantsApi().getRestaurants(gid.toString())
        matchedInfo = Gson().fromJson(res.matched.toString(), Matched::class.java)
    }

    if (isMatchCompleted(matchedInfo, groupInfo)) {
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = { onNavigateToMatchedRestaurants() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                Text("View Match Results!")
            }
    } else {
        val isEnabled = userCanViewPrompts(matchedInfo, groupInfo)
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = { onNavigateToRestaurantPrompts() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            enabled = isEnabled) {
                val text: String =
                    if (isEnabled) {
                        "Start Swiping"
                    } else if (userHasCompletedSwiping(matchedInfo)) {
                        "Wait for others in the group to finish liking!"
                    } else {
                        "Wait for admin to start the match process!"
                    }
                Text(text)
            }
    }
}

@Composable
fun HeaderSection(
    groupInfo: GroupInfo,
    group: Group,
    onNavigateToGroupSettings: (Group) -> Unit,
    onNavigateToRestaurantPrompts: () -> Unit,
    onNavigateToMatchedRestaurants: () -> Unit
) {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(250.dp)
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF2196F3), Color(0xFF21CBF3))))) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()) {
                    val painter =
                        if (groupInfo.imageUrl.isNullOrEmpty()) {
                            painterResource(id = R.drawable.ic_group_default)
                        } else {
                            rememberAsyncImagePainter(model = groupInfo.imageUrl)
                        }
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(150.dp).clip(CircleShape))
                    Text(
                        text = groupInfo.groupName,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black)
                    ActionButton(
                        group.gid,
                        groupInfo,
                        onNavigateToRestaurantPrompts,
                        onNavigateToMatchedRestaurants)
                }
            IconButton(
                onClick = { onNavigateToGroupSettings(group) },
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp))
                }
        }
}

@Composable
fun ContentSection(groupInfo: GroupInfo) {
    Column(
        modifier =
            Modifier.fillMaxSize()
                .padding(16.dp)
                .background(
                    Color(0xfffaebd7),
                    shape =
                        RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp,
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp))
                .padding(16.dp) // Add padding inside the column
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Members",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                items(groupInfo.members) { member ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            val icon =
                                if (member.isAdmin) Icons.Default.Star else Icons.Default.Person
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = member.name,
                                fontSize = 16.sp,
                                fontWeight =
                                    if (member.isAdmin) FontWeight.Bold else FontWeight.Normal)
                            if (member.isAdmin) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "(Admin)",
                                    fontSize = 16.sp,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold)
                            }
                        }
                }
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(icon = Icons.Default.List, label = "Preferences", value = groupInfo.preferences)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Location",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp))
            GoogleMap(
                modifier =
                    Modifier.height((LocalConfiguration.current.screenHeightDp * 0.60).dp)
                        .width((LocalConfiguration.current.screenWidthDp * 0.90).dp),
                cameraPositionState =
                    rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(groupInfo.location, 10f)
                    })
        }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = value.ifEmpty { "None" }, fontSize = 16.sp)
            }
        }
}
