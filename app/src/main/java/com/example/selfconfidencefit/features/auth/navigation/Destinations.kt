package com.example.selfconfidencefit.features.auth.navigation

sealed class Destinations(val route: String)  {
    object Login: Destinations("login_screen")
    object Register: Destinations("register_screen")
    object Home: Destinations("home_screen")
    object ForgotPassword: Destinations("forgot_password_screen")

}