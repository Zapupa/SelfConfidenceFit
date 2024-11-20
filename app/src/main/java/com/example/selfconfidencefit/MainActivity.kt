package com.example.selfconfidencefit

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.selfconfidencefit.di.MainModule.provideDatabase
import com.example.selfconfidencefit.receiver.DateChangedBroadcastReceiver
import com.example.selfconfidencefit.ui.presentation.navigation.Screen
import com.example.selfconfidencefit.ui.presentation.navigation.SetupNavHost
import com.example.selfconfidencefit.ui.theme.Gray
import com.example.selfconfidencefit.ui.theme.Purple40
import com.example.selfconfidencefit.ui.theme.SelfConfidenceFitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dateChangedBroadcastReceiver = DateChangedBroadcastReceiver(provideDatabase(application))
        this.registerReceiver(dateChangedBroadcastReceiver, IntentFilter(Intent.ACTION_DATE_CHANGED))
        enableEdgeToEdge()
        setContent {
            SelfConfidenceFitTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomAppBar(
                            containerColor = Color.Black,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                IconButton(
                                    onClick = { navController.navigate(Screen.Main.route) }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.homeicon),
                                        contentDescription = "Localized description",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { /* do something */ }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.exercisesicon),
                                        contentDescription = "Localized description",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { /* do something */ }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.foodicon),
                                        contentDescription = "Localized description",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { /* do something */ }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.profileicon),
                                        contentDescription = "Localized description",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )
                                }
                            }
                        }
                    },
                    content ={ innerPadding->
                        Surface(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            SetupNavHost(navController = navController)
                        }
                    }
                )
            }
        }
    }
}

