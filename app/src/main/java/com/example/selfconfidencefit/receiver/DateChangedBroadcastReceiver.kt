package com.example.selfconfidencefit.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.data.local.DatabaseApp
import com.example.selfconfidencefit.data.local.dao.StepsDao
import com.example.selfconfidencefit.data.local.models.StepsDay
import com.example.selfconfidencefit.di.MainModule
import com.example.selfconfidencefit.utils.DateFormat
import com.example.selfconfidencefit.viewmodel.StepsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DateChangedBroadcastReceiver(): BroadcastReceiver(){

    @Inject
    lateinit var stepsDao: StepsDao

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action == Intent.ACTION_DATE_CHANGED){
            @SuppressLint("SimpleDateFormat")
            val newDate = DateFormat.standardFormat(Calendar.getInstance().time)

            CoroutineScope(Dispatchers.IO).launch {
                stepsDao.insertStepsDay(
                    StepsDay(
                        0,
                        0,
                        newDate
                    )
                )
            }
        }
    }

}