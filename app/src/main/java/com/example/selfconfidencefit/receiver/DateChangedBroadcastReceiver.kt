package com.example.selfconfidencefit.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.selfconfidencefit.data.local.dao.pedometer.StepsDao
import com.example.selfconfidencefit.data.local.models.pedometer.StepsDay
import com.example.selfconfidencefit.utils.DateFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
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