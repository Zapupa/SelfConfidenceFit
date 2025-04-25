package com.example.selfconfidencefit.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.selfconfidencefit.features.pedometer.service.StepsSensorService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            context.startService(Intent(context, StepsSensorService::class.java))
        }
    }
}