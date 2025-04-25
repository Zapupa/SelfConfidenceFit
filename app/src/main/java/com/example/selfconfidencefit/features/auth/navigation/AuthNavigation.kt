package com.example.selfconfidencefit.features.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.selfconfidencefit.features.auth.AuthViewModel
import com.example.selfconfidencefit.ui.presentation.screens.auth.ForgotPasswordScreen
import com.example.selfconfidencefit.ui.presentation.screens.auth.LoginScreen
import com.example.selfconfidencefit.ui.presentation.screens.auth.RegistrationScreen
import com.example.selfconfidencefit.ui.presentation.screens.home.MainScreen

@Composable
fun AuthNavigation(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()

    // Проверка авторизации при старте
    LaunchedEffect(Unit) {
        if (authViewModel.isUserLoggedIn()) {
            navController.navigate(Destinations.Home.route) {
                popUpTo(Destinations.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destinations.Login.route
    ) {
        composable(Destinations.Register.route) {
            RegistrationScreen(
                onRegisterSuccess = {
                    navController.navigate(Destinations.Home.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Destinations.Login.route) },
                navController = navController
            )
        }
        composable(Destinations.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Destinations.Home.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Destinations.Register.route) },
                onForgotPassword = { navController.navigate(Destinations.ForgotPassword.route) },
                navController = navController

            )
        }
        composable(Destinations.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(Destinations.Home.route) {
            MainScreen(
                onSignOut = {
                    navController.navigate(Destinations.Login.route) {
                        popUpTo(Destinations.Home.route) { inclusive = true }
                    }
                },
                navController = navController
            )

        }
    }
}
