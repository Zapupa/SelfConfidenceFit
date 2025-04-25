package com.example.selfconfidencefit.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@Composable
fun SetupNavHost(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ){
        composable(route = Screen.Main.route){
            //MainScreen(navController = navController)
        }
    }
}