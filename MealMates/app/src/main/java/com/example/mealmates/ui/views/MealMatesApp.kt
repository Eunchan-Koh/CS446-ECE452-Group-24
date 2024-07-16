package com.example.mealmates.ui.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mealmates.constants.Routes
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.md_theme_dark_onPrimary
import com.example.mealmates.ui.theme.md_theme_light_secondary
import com.example.mealmates.ui.viewModels.LoginViewModel

data class NavigationItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter",
    "SuspiciousIndentation"
)
@Composable
fun MealMatesApp(loginModel: LoginViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var showBottomBar by rememberSaveable { mutableStateOf(true) }

    val navBarItems = arrayOf(
        NavigationItem(
            icon = Icons.Rounded.Home,
            label = "Home",
            route = Routes.HOME
        ),
        NavigationItem(
            icon = Icons.Rounded.Settings,
            label = "Profile",
            route = Routes.PROFILE
        )
    )

    fun onNavigateToSurvey() {
        navController.navigate(Routes.SURVEY)
    }

    fun onNavigateToMainPage() {
        navController.navigate(Routes.HOME)
    }

    fun onNavigateToRestaurantPrompts() {
        navController.navigate(Routes.RESTAURANT_PROMPTS)
    }

    fun onNavigateToMatchedRestaurants() {
        navController.navigate(Routes.MATCHED_RESTAURANTS)
    }

    fun onNavigateToGroup() {
        navController.navigate(Routes.GROUP)
    }

    fun onNavigateToGroupMembers() {
        navController.navigate(Routes.GROUP_MEMBERS)
    }

    fun onNavigateToProfile() {
        navController.navigate(Routes.PROFILE)
    }

    // test places api
    fun searchNearbyPlaces() {
        navController.navigate(Routes.PLACES_API_TEST)
    }

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        "survey" -> false
        else -> true
    }


    MealMatesTheme {
        Scaffold(
            bottomBar = {
                if (showBottomBar)
                BottomNavigation(
                    backgroundColor = md_theme_light_secondary,
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
                ) {
                    val currentDestination = navBackStackEntry?.destination

                    navBarItems.forEach { navItem ->
                        BottomNavigationItem(
                            modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp),
                            icon = {
                                Icon(
                                    navItem.icon,
                                    contentDescription = null,
                                    tint = md_theme_dark_onPrimary,
                                )
                            },
                            label = {
                                Text(
                                    navItem.label,
                                    maxLines = 1
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                            onClick = {
                                navController.navigate(navItem.route)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(0.dp)
            ) {

                NavHost(
                    navController,
                    startDestination = Routes.SURVEY
                ) {
                    composable(Routes.HOME) {
                        MainPage(loginModel, { onNavigateToRestaurantPrompts() },
                            { onNavigateToGroup() })
                    }

                    composable(Routes.SURVEY) {
                        PreferenceAndRestrictions(loginModel) { onNavigateToMainPage() }
                    }

                    composable(Routes.RESTAURANT_PROMPTS) {
                        RestaurantPrompt(loginModel) { onNavigateToMatchedRestaurants() }
                    }

                    composable(Routes.MATCHED_RESTAURANTS) {
                        ListOfMatchedRestaurantsPage(loginModel) { onNavigateToMainPage() }
                    }

                    composable(Routes.GROUP) {
                        GroupPage(loginModel, { onNavigateToGroupMembers() }, { searchNearbyPlaces() })
                    }

                    composable(Routes.GROUP_MEMBERS) {
                        GroupMembersPage(loginModel)

                    }

                    composable(Routes.PROFILE) {
                        UserProfileManagementPage(loginModel) { onNavigateToSurvey() }
                    }

                    // test
                    composable(Routes.PLACES_API_TEST) {
                        PlacesTest(loginModel)
                    }
                }
            }
        }
    }
}