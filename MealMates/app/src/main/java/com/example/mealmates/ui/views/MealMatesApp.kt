package com.example.mealmates.ui.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mealmates.constants.Routes
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.viewModels.LoginViewModel

data class NavigationItem(
    val icon: ImageVector,
    val label: String,
    val route: String
)

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("RememberReturnType", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MealMatesApp(loginModel: LoginViewModel) {
    val navController = rememberNavController()

    val navBarItems = arrayOf(
        NavigationItem(
            icon = Icons.Rounded.Home,
            label = "Home",
            route = Routes.HOME
        ),
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

    MealMatesTheme {
        Scaffold(
//            bottomBar = {
//                BottomNavigation(
//                    backgroundColor = MaterialTheme.colorScheme.tertiary,
//                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
//                ) {
//
//                    val navBackStackEntry by navController.currentBackStackEntryAsState()
//                    val currentDestination = navBackStackEntry?.destination
//
//                    navBarItems.forEach { navItem ->
//                        BottomNavigationItem(
//                            modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp),
//                            icon = {
//                                Icon(
//                                    navItem.icon,
//                                    contentDescription = null,
//                                    tint = MaterialTheme.colorScheme.primary,
//                                )
//                            },
//                            label = {
//                                Text(
//                                    navItem.label,
//                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
//                                    maxLines = 1
//                                )
//                            },
//                            selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
//                            onClick = {
//                                navController.navigate(navItem.route)
//                            }
//                        )
//                    }
//                }
//            }
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
                        MainPage(loginModel) { onNavigateToRestaurantPrompts() }
                    }

                    composable(Routes.SURVEY) {
                        PreferenceAndRestrictions(loginModel) { onNavigateToMainPage() }
                    }

                    composable(Routes.RESTAURANT_PROMPTS) {
                        RestaurantPrompt(loginModel) { onNavigateToMatchedRestaurants() }
                    }

                    composable(Routes.MATCHED_RESTAURANTS) {
                        ListOfMatchedRestaurantsPage(loginModel)
                    }
                }
            }
        }
    }
}