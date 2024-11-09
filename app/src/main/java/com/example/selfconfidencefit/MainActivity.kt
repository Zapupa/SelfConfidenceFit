package com.example.selfconfidencefit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.selfconfidencefit.ui.presentation.navigation.Screen
import com.example.selfconfidencefit.ui.presentation.navigation.SetupNavHost
import com.example.selfconfidencefit.ui.theme.Purple40
import com.example.selfconfidencefit.ui.theme.SelfConfidenceFitTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SelfConfidenceFitTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomAppBar(
                            actions = {
                                IconButton(onClick = { /* do something */ }) {
                                    Icon(Icons.Filled.Check, contentDescription = "Localized description")
                                }
                            }
                        )
                    }
                ){ innerPadding ->
                    Text(
                        modifier = Modifier.padding(innerPadding),
                        text = "Example of a scaffold with a bottom app bar."
                    )
                }
            }
        }
    }
}

