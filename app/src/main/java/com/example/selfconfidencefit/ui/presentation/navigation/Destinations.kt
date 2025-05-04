package com.example.selfconfidencefit.ui.presentation.navigation

sealed class Destinations(val route: String)  {
    object Login: Destinations("login_screen")
    object Register: Destinations("register_screen")
    object ForgotPassword: Destinations("forgot_password_screen")

    object Home: Destinations("home_screen")

    object WorkoutPlans: Destinations("workout_plans")
    object CreateWorkoutPlan: Destinations("create_workout_plan")
    object CreateExercise: Destinations("create_exercise")
    object WorkoutExecution: Destinations("workout_execution")
    object ExerciseExecution: Destinations("execution")
}