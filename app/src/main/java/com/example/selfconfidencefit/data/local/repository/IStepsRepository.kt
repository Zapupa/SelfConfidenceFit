package com.example.selfconfidencefit.data.local.repository

import androidx.lifecycle.LiveData
import com.example.selfconfidencefit.data.local.models.StepsDay
import com.example.selfconfidencefit.data.local.models.StepsGoal

interface IStepsRepository {
    //Шаги
    suspend fun insertStepsDay(steps: StepsDay, onSuccess: ()-> Unit)

    suspend fun updateStepsDay(steps: StepsDay, onSuccess: ()-> Unit)

    suspend fun addLatestSteps(stepsToAdd: Int, onSuccess: ()-> Unit)

    fun getStepsDay(key: Long): LiveData<StepsDay?>

    val getAllStepsDays: LiveData<List<StepsDay>>

    val getLatestStepsDay: LiveData<StepsDay> //val?

    val getLatestStepsDayObservable: LiveData<StepsDay> //val

    suspend fun deleteAllStepsDaysButLatest(onSuccess: ()-> Unit)

    suspend fun deleteAllStepsDaysButSeven(onSuccess: ()-> Unit)

    //Цель
    suspend fun insertGoal(goal: StepsGoal, onSuccess: ()-> Unit)

    suspend fun updateGoal(goal: StepsGoal, onSuccess: ()-> Unit)

    suspend fun deleteGoal(goal: StepsGoal, onSuccess: ()-> Unit)

    fun getGoal(key: Long): LiveData<StepsGoal?>

    val getAllGoals: LiveData<List<StepsGoal>>

}