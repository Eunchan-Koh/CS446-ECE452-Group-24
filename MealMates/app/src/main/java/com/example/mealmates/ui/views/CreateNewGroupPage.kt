package com.example.mealmates.ui.views

import TextInput
import android.widget.Toast
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mealmates.apiCalls.UserApi
import com.example.mealmates.constants.GlobalObjects
import com.example.mealmates.models.User
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.button_colour
import com.example.mealmates.ui.viewModels.LoginViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.mealmates.apiCalls.GroupApi
import com.example.mealmates.models.GetPlaceDetailsResponse
import com.example.mealmates.models.Group
import com.example.mealmates.ui.theme.md_theme_light_primary
import com.google.android.gms.maps.model.LatLng

@Composable
fun CreateNewGroupPage(loginModel: LoginViewModel, onNavigateToMainPage: () -> Unit = {}) {
    val userCur = UserApi().getUser(GlobalObjects.user.id!!)
    var tempGroupName by remember { mutableStateOf("") }
    var tempGroupLocation = userCur.location
    var tempGroupProfilePic by remember { mutableStateOf(byteArrayOf(0)) }
    val (tempGroupMembers, setTempGroupMembers) = remember {
        mutableStateOf(listOf(userCur))
    }

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
            CancelAndCreateRow(onNavigateToMainPage, tempGroupName, tempGroupLocation, tempGroupProfilePic, tempGroupMembers)
            ChooseGroupPicture(null)
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
                OutlinedTextField(
                    value = tempGroupName,
                    onValueChange = { tempGroupName = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedLabelColor = md_theme_light_primary,
                        unfocusedLeadingIconColor = Color.White
                    ),
                    singleLine = true,
                )
            }
            GroupMembersSection(tempGroupMembers, setTempGroupMembers, userCur)
        }
    }
}

@Composable
fun CancelAndCreateRow(
    onNavigateToMainPage: () -> Unit,
    tempGroupName: String,
    tempGroupLocation: LatLng,
    tempGroupProfilePic: ByteArray,
    tempGroupMembers: List<User>
) {
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
            onClick = { setupNewGroupInfo(onNavigateToMainPage, tempGroupName, tempGroupLocation, tempGroupProfilePic, tempGroupMembers) },
            colors = ButtonDefaults.buttonColors(containerColor = button_colour),
            modifier = Modifier.height(40.dp),
            shape = CircleShape,
            enabled = tempGroupName != ""
        ) {
            Text("Create")
        }
    }
}

fun setupNewGroupInfo(
    onNavigateToMainPage: () -> Unit,
    tempGroupName: String,
    tempGroupLocation: LatLng,
    tempGroupProfilePic: ByteArray,
    tempGroupMembers: List<User>
) {
    var groupUIDs = mutableListOf<String>()
    var adminUser = tempGroupMembers[0]
    var groupPreferences = adminUser.preferences
    var groupRestrictions = adminUser.restrictions
    for (user in tempGroupMembers) {
        groupUIDs.add(user.id!!)
        groupPreferences = groupPreferences.intersect(user.preferences.toSet()).toList()
        groupRestrictions = groupRestrictions.union(user.restrictions.toSet()).toList()
    }
    val gid = 0
    val newGroup = Group(gid, tempGroupName, groupUIDs, groupPreferences, groupRestrictions, tempGroupProfilePic, tempGroupLocation)
    GroupApi().createGroup(newGroup)
    onNavigateToMainPage()
}

@Composable
fun ChooseGroupProfilePic() {
    val curCon = LocalContext.current
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .clickable {
                Toast
                    .makeText(
                        curCon, "Clicked on Image Selection area!",
                        Toast.LENGTH_SHORT
                    )
                    .show()
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
fun GroupMembersSection(
    tempGroupMembers: List<User>,
    setTempGroupMembers: (List<User>) -> Unit,
    userCur: User
) {
    var currentGroupMemberSize = tempGroupMembers.size
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 18.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                        icon = Icons.Default.AccountBox,
                        tempGroupMembers = tempGroupMembers,
                        setTempGroupMembers = setTempGroupMembers
                    )
                };
            }
            Text(
                text = "Group Members",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_primary,
            )
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
        Spacer(modifier = Modifier.height(8.dp))
        for (groupUser in tempGroupMembers) {
            var curGroupUserName = groupUser.name
            var curGroupUserEmail = groupUser.email
            var curGroupUserPhoto = groupUser.image

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
                        text = curGroupUserName,
                        fontWeight = FontWeight.SemiBold,
                    )

                    if (userCur.id == groupUser.id) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Admin",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black
                        )
                    }

                }
                if (userCur.id != groupUser.id) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "remove $curGroupUserName from group",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp)
                            .clickable {
                                removeTempGroupMember(groupUser.id, setTempGroupMembers, tempGroupMembers)
                            }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

fun removeTempGroupMember(
    groupUserId: String?,
    setTempGroupMembers: (List<User>) -> Unit,
    tempGroupMembers: List<User>
) {
    val newGroupUsers = tempGroupMembers.filter { user -> user.id != groupUserId }
    setTempGroupMembers(newGroupUsers)
}

@Composable
fun AlertDialogAddNewMember(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    tempGroupMembers: List<User>,
    setTempGroupMembers: (List<User>) -> Unit
) {
    var newMemberEmail by remember { mutableStateOf("") }
    var searchNewUserFailure by remember { mutableStateOf(false) }

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
                    onValueChange = { searchNewUserFailure = false
                        newMemberEmail = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = searchNewUserFailure,
                    supportingText = {
                        if (searchNewUserFailure) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "User with email '${newMemberEmail}' does not exist",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (!searchUserByEmail(newMemberEmail, tempGroupMembers, setTempGroupMembers)) {
                        searchNewUserFailure = true
                    } else {
                        onConfirmation()
                    }
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

fun searchUserByEmail(
    newMemberEmail: String,
    tempGroupMembers: List<User>,
    setTempGroupMembers: (List<User>) -> Unit
): Boolean {
    var newUser = UserApi().getUserByEmail(newMemberEmail)
    if (newUser != User()) {
        var newTempGroupMembers = tempGroupMembers.plus(newUser)
        setTempGroupMembers(newTempGroupMembers)
        return true
    } else {
        return false
    }
}
