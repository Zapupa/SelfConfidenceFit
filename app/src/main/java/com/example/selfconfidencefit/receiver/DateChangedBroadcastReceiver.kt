package com.example.selfconfidencefit.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.selfconfidencefit.data.local.DatabaseApp
import com.example.selfconfidencefit.data.local.models.StepsDay
import com.example.selfconfidencefit.di.MainModule
import com.example.selfconfidencefit.utils.DateFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DateChangedBroadcastReceiver @Inject constructor(private val database: DatabaseApp): BroadcastReceiver(){
    private var receiverJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + receiverJob)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if(Intent.ACTION_DATE_CHANGED == action){
            @SuppressLint("SimpleDateFormat")
            val newDate = DateFormat.standardFormat(Calendar.getInstance().time)
            uiScope.launch{
                withContext(Dispatchers.IO){
                    database.stepsDao.insertStepsDay(
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

}