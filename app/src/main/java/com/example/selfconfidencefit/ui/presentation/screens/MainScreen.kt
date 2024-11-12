package com.example.selfconfidencefit.ui.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Text
import com.example.selfconfidencefit.viewmodel.StepsViewModel

@Composable
fun MainScreen(navController: NavController){
    val day : String= "day"
    Card(
        modifier = Modifier
            .padding(18.dp, 0.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Red),
    ) {
        Column(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = "Шаги за сегодня"
                )

                Text(
                    text = "placeholder"
                )
            }

            Row(
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                ) {

                }

                Column(
                    modifier = Modifier
                ) {

                }
            }
        }
    }

}
