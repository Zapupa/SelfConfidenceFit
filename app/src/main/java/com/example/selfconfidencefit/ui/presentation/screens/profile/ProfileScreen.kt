package com.example.selfconfidencefit.ui.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.selfconfidencefit.features.auth.AuthViewModel

@Composable
fun ProfileScreen(
    onSignOut: ()-> Unit,
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.material3.Text(
            text = "Добро пожаловать!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        val currentUser = authViewModel.auth.currentUser
        currentUser?.let { user ->
            androidx.compose.material3.Text("Вы вошли как: ${user.email}")
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                authViewModel.auth.signOut()
                onSignOut()
            }
        ) {
            androidx.compose.material3.Text("Выйти")
        }
    }
}