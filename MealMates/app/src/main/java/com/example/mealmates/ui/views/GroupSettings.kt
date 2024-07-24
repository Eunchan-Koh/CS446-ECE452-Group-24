package com.example.mealmates.ui.views

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mealmates.R
import com.example.mealmates.apiCalls.GroupApi
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.constants.RESTAURANT_TYPE_LABEL_LIST
import com.example.mealmates.models.Group
import com.example.mealmates.models.User
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GroupSettings(
    loginModel: LoginViewModel,
    gid: Int,
    name: String,
    preferences: List<String>,
    restrictions: List<String>,
    uids: List<String>,
    image: ByteArray,
    location: LatLng,
    onNavigateToGroupInfo: (Group) -> Unit = {}
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

    val groupInfo =
        GroupInfo(
            imageUrl = null,
            groupName = name,
            members = users,
            preferences = formattedPreferences.joinToString(", "),
            restrictions = formattedPreferences.joinToString(", "),
            location = location
        )

    val curGroup = GroupApi().getGroup(gid.toString())
    val curUserID = GlobalObjects.user.id.toString()

    MealMatesTheme {
        Box(
            Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .fillMaxHeight()
                    .verticalScroll(ScrollState(0)),
            ) {

                TopBar({ onNavigateToGroupInfo(curGroup) }, curGroup)

                ChooseGroupPicture(groupInfo)

                var new_group_name by rememberSaveable { mutableStateOf(name) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Group Name",
                        color = md_theme_light_primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    if (uids[0] == curUserID) {
                        OutlinedTextField(
                            value = new_group_name,
                            onValueChange = { new_group_name = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedLabelColor = md_theme_light_primary,
                                unfocusedLeadingIconColor = Color.White
                            ),
                            singleLine = true,
                        )
                    } else {
                        OutlinedTextField(
                            value = new_group_name,
                            onValueChange = { new_group_name = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedLabelColor = md_theme_light_primary,
                                unfocusedLeadingIconColor = Color.White
                            ),
                            singleLine = true,
                            readOnly = true,
                        )
                    }
                }
//                GroupName(uids, curUserID, name, new_group_name)

                GroupMembers(groupInfo, (uids[0] == curUserID), gid)

                FoodPreferences(groupInfo)

                GroupLocation(groupInfo, uids, curUserID)

                Spacer(modifier = Modifier.height(20.dp))

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {

                    if (uids[0] == curUserID) {
                        Text(
                            text = "Delete Group",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.clickable {
                                GroupApi().deleteGroup(gid.toString())
                            },
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline,
                            color = Color.Red,
                        )
                    } else {
                        Text(
                            text = "Leave Group",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.clickable {
                                GroupApi().deleteUserFromGroup(curUserID, gid.toString())
                            },
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline,
                            color = Color.Red,
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    SaveButton(loginModel, { onNavigateToGroupInfo(curGroup) }, curGroup, new_group_name)
                }
            }
        }
    }
}

@Composable
fun TopBar(onNavigateToGroupInfo: (Group) -> Unit, group: Group){
    Box {
        IconButton(
            onClick = { onNavigateToGroupInfo(group) },
            modifier = Modifier.align(Alignment.TopStart)) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.Black,
                modifier = Modifier
                    .size(40.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Group Settings",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_primary,
            )
        }
    }
}

@Composable
fun ChooseGroupPicture(groupInfo: GroupInfo) {
    ChooseGroupPicture(imageUrl = groupInfo.imageUrl)
}

@Composable
fun ChooseGroupPicture(imageUrl: String?) {
    val curCon = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(12.dp)
            .clickable {
                Toast
                    .makeText(
                        curCon, "Clicked on Image Selection area!",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter =
            if (imageUrl.isNullOrEmpty()) {
                painterResource(id = R.drawable.ic_group_default)
            } else {
                rememberAsyncImagePainter(model = imageUrl)
            }

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(110.dp)
                .aspectRatio(1F, matchHeightConstraintsFirst = false)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = CircleShape
                )
                .clip(CircleShape)
        )
    }
//    val curCon = LocalContext.current
//    Column(
//        modifier = Modifier
//            .clickable {
//                Toast.makeText(curCon, "Clicked on Image Selection area!",
//                    Toast.LENGTH_SHORT).show()
//            }
//    ) {
//        var UserImage = BitmapFactory.decodeByteArray(image, 0, image.size)
//        if (UserImage!=null)
//            Image(bitmap = UserImage.asImageBitmap(), contentDescription = "")
//        else
//            Icon(
//                Icons.Default.AddCircle,
//                "add",
//                tint = Color.LightGray,
//                modifier = Modifier
//                    .padding(10.dp)
//                    .size(110.dp),
//            )
//    }
}

@Composable
fun GroupName(uids: List<String>, curUserID: String, groupName: String, new_group_namea: String) {
    var new_group_name by rememberSaveable { mutableStateOf(groupName) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Group Name",
            color = md_theme_light_primary,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        if (uids[0] == curUserID) {
            OutlinedTextField(
                value = new_group_name,
                onValueChange = { new_group_name = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = md_theme_light_primary,
                    unfocusedLeadingIconColor = Color.White
                ),
                singleLine = true,
            )
        } else {
            OutlinedTextField(
                value = new_group_name,
                onValueChange = { new_group_name = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = md_theme_light_primary,
                    unfocusedLeadingIconColor = Color.White
                ),
                singleLine = true,
                readOnly = true,
            )
        }
    }
}

@Composable
fun GroupMembers(
    groupInfo: GroupInfo,
    isAdmin: Boolean,
    gid: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val openNewMemberDialog = remember { mutableStateOf(false) };
            when {
                openNewMemberDialog.value -> {
                    AlertDialogAddNewMember(
                        onDismissRequest = { openNewMemberDialog.value = false },
                        onConfirmation = {
                            openNewMemberDialog.value = false
                            println("Confirmation registered") // Add logic here to handle confirmation.
                        },
                        dialogTitle = "Add Member",
                        dialogText = "Member Email Address",
                        icon = Icons.Default.AccountBox
                    )
                };
            }
            Text(
                text = "Group Members",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_primary,
            )

            if (isAdmin) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "back",
                    tint = md_theme_light_primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            openNewMemberDialog.value = !openNewMemberDialog.value
                        }
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))

        for (member in groupInfo.members) {
            var member_name = member.name

            val openRemoveMemberDialog = remember { mutableStateOf(false) };
            when {
                openRemoveMemberDialog.value -> {
                    AlertDialogDeleteRemoveMember(
                        onDismissRequest = { openRemoveMemberDialog.value = false },
                        onConfirmation = {
                            GroupApi().deleteUserFromGroup(member.uid, gid.toString())
                            openRemoveMemberDialog.value = false
                            println("Confirmation registered") // Add logic here to handle confirmation.
                        },
                        dialogTitle = "Remove Member",
                        dialogText = "Are you sure you want to remove $member_name from the group?",
                        icon = Icons.Default.AccountBox
                    )
                };
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier
                            .size(50.dp)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = member.name,
                        fontWeight = FontWeight.SemiBold,
                    )

                    if (member.isAdmin)  {
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Admin",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black
                        )
                    }

                }
                if (isAdmin) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "remove $member_name from group",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp)
                            .clickable {
                                openRemoveMemberDialog.value = !openRemoveMemberDialog.value
//                                notification.value = "remove $member_name from group"
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun FoodPreferences(groupInfo: GroupInfo) {
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "Preferences",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = md_theme_light_primary,
    )
    Text(
        text = groupInfo.preferences,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
    )
}

@Composable
fun GroupLocation(groupInfo: GroupInfo, uids: List<String>, curUserID: String) {
    GroupLocation(groupInfo.location, uids[0] == curUserID, true)
}

    @SuppressLint("UnrememberedMutableState")
    @Composable
fun GroupLocation(groupLocation: LatLng, isAdmin: Boolean, isExistingGroup: Boolean) {
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Group Location",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = md_theme_light_primary,
        )
        if (isAdmin && isExistingGroup) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "back",
                tint = md_theme_light_primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // TODO: Add ability to search for new location
                    }
            )
        }
    }
    Spacer(modifier = Modifier.height(5.dp))
    GoogleMap(
        modifier =
        Modifier
            .height(250.dp)
            .width((LocalConfiguration.current.screenWidthDp * 0.90).dp),
        cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(groupLocation, 10f)
        }) {
        Marker(
            state = MarkerState(groupLocation),
            title = "Your Location",
            snippet = "Your location"
        )
    }
}

@Composable
fun SaveButton(loginModel: LoginViewModel, onNavigateToGroupInfo: (Group) -> Unit = {}, group: Group, new_group_name: String) {
    group.name = new_group_name;

    Button(
        onClick = {
            /* Handle Save */
            GroupApi().updateGroup(group)
            onNavigateToGroupInfo(group)
        },
        colors = ButtonDefaults.buttonColors(
            md_theme_light_primary
        ),
        modifier = Modifier
            .height(50.dp)
        ,
        shape = CircleShape
    ) {
        Text(text = "Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogAddNewMember(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    var newMemberEmail by remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = "Example Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(50.dp),
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                TextField(
                    value = newMemberEmail,
                    onValueChange = { newMemberEmail = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogDeleteRemoveMember(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = "Warning Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(50.dp),
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
