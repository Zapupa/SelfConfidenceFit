package com.example.selfconfidencefit.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.selfconfidencefit.features.auth.AuthViewModel
import com.example.selfconfidencefit.ui.presentation.screens.auth.ForgotPasswordScreen
import com.example.selfconfidencefit.ui.presentation.screens.auth.LoginScreen
import com.example.selfconfidencefit.ui.presentation.screens.auth.RegistrationScreen
import com.example.selfconfidencefit.ui.presentation.screens.home.MainScreen
import com.example.selfconfidencefit.ui.presentation.screens.workout.AddExerciseScreen
import com.example.selfconfidencefit.ui.presentation.screens.workout.CreateWorkoutPlanScreen
import com.example.selfconfidencefit.ui.presentation.screens.workout.WorkoutExecutionScreen
import com.example.selfconfidencefit.ui.presentation.screens.workout.WorkoutPlansScreen

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

        //Workout plan
        composable(Destinations.WorkoutPlans.route) {
            WorkoutPlansScreen(
                onCreateNewPlan = { navController.navigate(Destinations.CreateWorkoutPlan.route) },
                onPlanSelected = { planId ->
                    navController.navigate("${Destinations.CreateWorkoutPlan.route}/$planId")
                }
            )
        }

        composable(Destinations.CreateWorkoutPlan.route) {
            CreateWorkoutPlanScreen(
                onBack = { navController.popBackStack() },
                onSaveComplete = { planId ->
                    navController.popBackStack()
                    navController.navigate("${Destinations.CreateWorkoutPlan.route}/$planId") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "${Destinations.CreateWorkoutPlan.route}/{planId}",
            arguments = listOf(navArgument("planId") { type = NavType.LongType })
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getLong("planId") ?: 0L
            WorkoutExecutionScreen(
                planId = planId,
                onFinish = { navController.popBackStack() }
            )
        }

        composable(Destinations.CreateExercise.route) {
            AddExerciseScreen(onBack = { navController.popBackStack() })
        }
    }
}


