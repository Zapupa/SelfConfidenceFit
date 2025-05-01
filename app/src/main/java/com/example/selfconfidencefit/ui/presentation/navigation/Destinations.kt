package com.example.selfconfidencefit.ui.presentation.navigation

sealed class Destinations(val route: String)  {
    object Login: Destinations("login_screen")
    object Register: Destinations("register_screen")
    object ForgotPassword: Destinations("forgot_password_screen")

    object Home: Destinations("home_screen")

    object WorkoutPlans: Destinations("workout_plans")
    object AddWorkoutPlan: Destinations("add_workout_plan")
    object AddExercise: Destinations("add_exercise")
    object WorkoutPlanDetails: Destinations("workout_plan_details")
    object ExerciseExecution: Destinations("execution")
}