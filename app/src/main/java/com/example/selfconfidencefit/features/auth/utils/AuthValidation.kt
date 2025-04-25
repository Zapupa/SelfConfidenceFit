package com.example.selfconfidencefit.features.auth.utils

import android.util.Patterns

object AuthValidation {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}