package com.example.selfconfidencefit

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.selfconfidencefit.features.pedometer.StepsViewModel
import com.example.selfconfidencefit.features.pedometer.service.StepsSensorService
import com.example.selfconfidencefit.ui.MainApp
import com.example.selfconfidencefit.ui.theme.SelfConfidenceFitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: StepsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startStepCounterService()

        enableEdgeToEdge()
        setContent {
            SelfConfidenceFitTheme {
                MainApp()
            }
        }
    }

    private fun startStepCounterService() {
        val serviceIntent = Intent(this, StepsSensorService::class.java)

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Для Android 8.0+
                startForegroundService(serviceIntent)
            } else {
                // Для старых версий
                startService(serviceIntent)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to start service", e)
            // Альтернативный запуск или обработка ошибки
        }
    }
}

