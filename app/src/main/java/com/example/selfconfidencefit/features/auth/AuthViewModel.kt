package com.example.selfconfidencefit.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    val auth: FirebaseAuth = Firebase.auth

    // Состояния регистрации
    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    // Состояния входа
    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    // Регистрация
    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _registerState.value = AuthState.Success
            } catch (e: Exception) {
                _registerState.value = AuthState.Error(
                    message = e.message ?: "Ошибка регистрации",
                    errorType = when (e) {
                        is FirebaseAuthWeakPasswordException -> ErrorType.WEAK_PASSWORD
                        is FirebaseAuthInvalidCredentialsException -> ErrorType.INVALID_EMAIL
                        is FirebaseAuthUserCollisionException -> ErrorType.EMAIL_IN_USE
                        else -> ErrorType.GENERIC
                    }
                )
            }
        }
    }


    // Вход
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _loginState.value = AuthState.Success
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(
                    message = e.message ?: "Ошибка входа",
                    errorType = when (e) {
                        is FirebaseAuthInvalidUserException -> ErrorType.USER_NOT_FOUND
                        is FirebaseAuthInvalidCredentialsException -> ErrorType.WRONG_PASSWORD
                        else -> ErrorType.GENERIC
                    }
                )
            }
        }
    }

    // Проверка авторизации при старте
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    // Отправляет Кода на почту
    fun sendPasswordResetEmail(email: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                onComplete()
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(
                    e.message ?: "Ошибка отправки письма",
                    ErrorType.GENERIC
                )
            }
        }
    }

    sealed class AuthState {
        data object Idle : AuthState()
        data object Loading : AuthState()
        data object Success : AuthState()
        data class Error(
            val message: String,
            val errorType: ErrorType
        ) : AuthState()
    }

    enum class ErrorType {
        WEAK_PASSWORD,
        INVALID_EMAIL,
        EMAIL_IN_USE,
        USER_NOT_FOUND,
        WRONG_PASSWORD,
        GENERIC
    }
}