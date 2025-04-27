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
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.selfconfidencefit.R
import com.example.selfconfidencefit.data.local.dao.StepsDao
import com.example.selfconfidencefit.data.local.models.StepsDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.selfconfidencefit.data.local.repository.StepsRepository
import com.example.selfconfidencefit.utils.DateFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class StepsSensorService : Service(), SensorEventListener {
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var currentDate = dateFormatter.format(Date())
    private var sensorManager: SensorManager? = null
    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null
    private var lastSteps = 0
    private var currentSteps = 0
    private val notificationId = 1
    private val channelId = "step_counter_channel"

    // Добавляем флаг для отслеживания состояния foreground
    private var isForeground = false

    @Inject lateinit var stepsDao: StepsDao

    private val dateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_DATE_CHANGED, Intent.ACTION_TIME_CHANGED -> {
                    checkAndUpdateDate()
                }
                Intent.ACTION_TIME_TICK -> {
                    // Проверяем полночь каждую минуту
                    if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == 0 &&
                        Calendar.getInstance().get(Calendar.MINUTE) == 0) {
                        checkAndUpdateDate()
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        initDateTracking()
        createNotificationChannel()
        startForegroundWithNotification()
        initSensors()
    }

    private fun initSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val sensorList = sensorManager!!.getSensorList(Sensor.TYPE_ALL)
        Log.d("StepsSensor", "Available sensors: ${sensorList.joinToString { it.name }}")


        stepCounterSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        when {
            stepCounterSensor != null -> {
                Log.d("StepsSensor", "Using STEP_COUNTER sensor")
                sensorManager!!.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
            }
            stepDetectorSensor != null -> {
                Log.d("StepsSensor", "Using STEP_DETECTOR sensor")
                sensorManager!!.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_UI)
            }
            else -> {
                Log.e("StepsSensor", "No step sensors available!")
                stopSelf() // Останавливаем сервис, если датчиков нет
            }

        }
    }

    private fun startForegroundWithNotification() {
        val notification = createNotification(10)
        startForeground(notificationId, notification)
        isForeground = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Если по какой-то причине сервис не в foreground, исправляем это
        if (!isForeground) {
            startForegroundWithNotification()
        }
        return START_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Step Counter",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Tracking your steps"
            }
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(steps: Int): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Step Counter")
            .setContentText("Steps today: $steps")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Убедитесь что этот ресурс существует
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun initDateTracking() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_TIME_TICK)
        }
        registerReceiver(dateChangeReceiver, filter)
    }

    private fun checkAndUpdateDate() {
        val newDate = dateFormatter.format(Date())
        if (newDate != currentDate) {
            // Сохраняем шаги за предыдущий день
            CoroutineScope(Dispatchers.IO).launch {
                saveCurrentSteps()
            }
            currentDate = newDate
            resetStepCounter()
        }
    }

    private suspend fun saveCurrentSteps() {
        val stepsEntity = stepsDao.getStepsByDate(currentDate) ?: StepsDay(date = currentDate, steps = 0)
        stepsDao.insertStepsDay(stepsEntity.copy(steps = stepsEntity.steps + currentSteps))
    }

    private fun resetStepCounter() {
        currentSteps = 0
        lastSteps = 0
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Обработка изменения точности сенсора
        Log.d("StepsSensor", "Accuracy changed: $accuracy")
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                // Для TYPE_STEP_COUNTER event.values[0] содержит общее количество шагов с момента регистрации
                val totalSteps = event.values[0].toInt()

                if (lastSteps == 0) {
                    // Первое получение значения
                    lastSteps = totalSteps
                } else {
                    // Вычисляем разницу с последним значением
                    val stepsDiff = totalSteps - lastSteps
                    currentSteps += stepsDiff
                    lastSteps = totalSteps

                    // Сохраняем шаги (например, в ViewModel или SharedPreferences)
                    CoroutineScope(Dispatchers.IO).launch {
                        saveCurrentSteps()
                    }
                }
            }

            Sensor.TYPE_STEP_DETECTOR -> {
                // Для TYPE_STEP_DETECTOR event.values[0] = 1.0 означает один шаг
                if (event.values[0] == 1.0f) {
                    currentSteps++
                    CoroutineScope(Dispatchers.IO).launch {
                        saveCurrentSteps()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(dateChangeReceiver)
        CoroutineScope(Dispatchers.IO).launch {
            saveCurrentSteps()
        }
    }
}