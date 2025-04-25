package com.example.selfconfidencefit.features.pedometer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.selfconfidencefit.data.local.models.StepsDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.selfconfidencefit.data.local.repository.StepsRepository
import com.example.selfconfidencefit.utils.DateFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class StepsSensorService : Service(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var currentSteps = 0
    private var lastSaveTime = 0L
    private val notificationId = 1
    private val channelId = "step_counter_channel"


    @Inject
    lateinit var repository: StepsRepository

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(notificationId, createNotification())

        initSensor()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_TIME_TICK)
        }
        registerReceiver(dateChangeReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
        unregisterReceiver(dateChangeReceiver)
        saveCurrentSteps()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            currentSteps = it.values[0].toInt()
            // Сохраняем каждые 10 минут или при закрытии
            if (System.currentTimeMillis() - lastSaveTime > 10 * 60 * 1000) {
                saveCurrentSteps()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun saveCurrentSteps() {
        val currentDate = DateFormat.standardFormat(Date())
        CoroutineScope(Dispatchers.IO).launch {
            val existingEntry = repository.getStepsByDate(currentDate)
            val totalSteps = existingEntry?.steps?.plus(currentSteps) ?: currentSteps
            repository.insertStepsDay(
                StepsDay(
                    date = currentDate,
                    steps = totalSteps
                )
            )
            lastSaveTime = System.currentTimeMillis()
        }
    }

    private val dateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIME_TICK) {
                val calendar = Calendar.getInstance()
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)

                // Проверяем полночь
                if (currentHour == 0 && currentMinute == 0) {
                    saveCurrentSteps()
                    currentSteps = 0 // Сбрасываем счетчик для нового дня
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Step Counter",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Tracks your steps"
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Step Counter")
            .setContentText("Tracking your steps...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun initSensor() {
        // Ваш код инициализации сенсора
    }
}