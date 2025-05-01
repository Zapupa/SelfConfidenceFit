package com.example.selfconfidencefit.ui.presentation.navigation

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
import com.example.selfconfidencefit.ui.presentation.screens.workout.AddExerciseScreen
import com.example.selfconfidencefit.ui.presentation.screens.workout.AddWorkoutPlanScreen
import com.example.selfconfidencefit.ui.presentation.screens.workout.WorkoutPlanDetailsScreen
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
                onNavigateToAddPlan = { navController.navigate(Destinations.AddWorkoutPlan.route) },
                onNavigateToPlanDetails = { planId ->
                    navController.navigate("workout_plan_details/$planId")
                }
            )
        }
        composable(Destinations.AddWorkoutPlan.route) {
            AddWorkoutPlanScreen(onBack = { navController.popBackStack() })
        }
        composable(Destinations.AddExercise.route) {
            AddExerciseScreen(onBack = { navController.popBackStack() })
        }
        composable("workout_plan_details/{workoutPlanId}") { backStackEntry ->
            val workoutPlanId = backStackEntry.arguments?.getString("workoutPlanId")?.toLongOrNull() ?: 0L
            WorkoutPlanDetailsScreen(
                workoutPlanId = workoutPlanId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}


