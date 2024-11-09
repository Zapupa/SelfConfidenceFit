package com.example.selfconfidencefit.ui.presentation.navigation

sealed class Screen(
    val route: String
){
    object Main : Screen("main")
}
