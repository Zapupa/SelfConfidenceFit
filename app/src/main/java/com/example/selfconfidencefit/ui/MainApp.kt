package com.example.selfconfidencefit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.selfconfidencefit.R
import com.example.selfconfidencefit.ui.presentation.navigation.AuthNavigation
import com.example.selfconfidencefit.ui.presentation.navigation.Destinations

@Composable
fun MainApp(){
    val navController = rememberNavController()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val bottomBarRoutes = listOf(
        Destinations.Home.route,
        Destinations.WorkoutPlans.route,
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if(showBottomBar){
                BottomAppBar(
                    containerColor = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        BottomNavigationItem(
                            selected = currentRoute == Destinations.Home.route,
                            onClick = { navController.navigate(Destinations.Home.route) },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.homeicon),
                                    contentDescription = "Localized description",
                                    tint = if (currentRoute == Destinations.Home.route) Color.Yellow else Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            },
                        )
                        BottomNavigationItem(
                            selected = false, //Not implemented
                            onClick = { navController.navigate(Destinations.WorkoutPlans.route) },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.exercisesicon),
                                    contentDescription = "Localized description",
                                    tint = if (currentRoute == Destinations.WorkoutPlans.route) Color.Yellow else Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            },
                            selectedContentColor = Color.Yellow,
                            unselectedContentColor = Color.White
                        )
                        BottomNavigationItem(
                            selected = false, //Not implemented
                            onClick = { navController.navigate(Destinations.Home.route) },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.foodicon),
                                    contentDescription = "Localized description",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            },
                            selectedContentColor = Color.Yellow,
                            unselectedContentColor = Color.White
                        )
                        BottomNavigationItem(
                            selected = false, //Not implemented
                            onClick = { navController.navigate(Destinations.Home.route) },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.profileicon),
                                    contentDescription = "Localized description",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                            },
                            selectedContentColor = Color.Yellow,
                            unselectedContentColor = Color.White
                        )

                    }
                }
            }
        },
        content ={ innerPadding->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
//                            SetupNavHost(navController = navController)
                AuthNavigation(navController = navController)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    MainApp()
}