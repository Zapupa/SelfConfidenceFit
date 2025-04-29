package com.example.selfconfidencefit.ui.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.example.selfconfidencefit.data.local.models.pedometer.StepsDay
import com.example.selfconfidencefit.features.auth.AuthViewModel
import com.example.selfconfidencefit.ui.theme.lightGray
import com.example.selfconfidencefit.features.pedometer.StepsViewModel

@Composable
fun MainScreen(
    onSignOut: ()-> Unit,
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
){
    val stepVM = hiltViewModel<StepsViewModel>()
    val stepDays = stepVM.readAllStepsDays().observeAsState(listOf()).value
    val lastDay = stepVM.readLatestStepsDayObservable().observeAsState().value

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(stepDays) { stepDay ->
            StepItem(stepDay = stepDay, viewModel = stepVM)
        }
    }
    Card(
        modifier = Modifier
            .padding(18.dp, 0.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = lightGray),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start

            ) {
                Text(
                    text = "Шаги за сегодня",
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Text(
                    text = lastDay?.steps.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 50.sp,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                ) {
                    Text(
                        text = "км",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 2.sp,
                        color = Color.White
                    )

                    Text(
                        text = "1,7",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                ) {
                    Text(
                        text = "цель",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 2.sp,
                        color = Color.White
                    )

                    Text(
                        text = "42%",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
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

@Composable
fun StepItem(stepDay: StepsDay, viewModel: StepsViewModel) {
    Card {
        Text(text = stepDay.date)
        Text(text = stepDay.steps.toString())
    }
}
