package com.example.selfconfidencefit.ui.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.wear.compose.material.Text
import com.example.selfconfidencefit.data.local.models.StepsDay
import com.example.selfconfidencefit.ui.theme.lightGray
import com.example.selfconfidencefit.viewmodel.StepsViewModel

@Composable
fun MainScreen(navController: NavController){
    val stepVM = hiltViewModel<StepsViewModel>()
    val stepDays = stepVM.readAllStepsDays().observeAsState(listOf()).value

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
                    text = "4 214",
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

}

@Composable
fun StepItem(stepDay: StepsDay, viewModel: StepsViewModel) {
    Card {
        Text(text = "fas")
        Text(text = stepDay.steps.toString())
    }
}
