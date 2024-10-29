package com.example.selfconfidencefit.data.local.repository

import androidx.lifecycle.LiveData
import com.example.selfconfidencefit.data.local.models.StepsDay
import com.example.selfconfidencefit.data.local.models.StepsGoal
import com.example.selfconfidencefit.data.local.DatabaseApp
import javax.inject.Inject

class StepsRepository @Inject constructor(private val database: DatabaseApp) : IStepsRepository{
    override suspend fun insertStepsDay(steps: StepsDay, onSuccess: () -> Unit) {
        database.stepsDao.insertStepsDay(steps)
        onSuccess()
    }

    override suspend fun updateStepsDay(steps: StepsDay, onSuccess: () -> Unit) {
        database.stepsDao.updateStepsDay(steps)
        onSuccess()
    }

    override suspend fun addLatestSteps(stepsToAdd: Int, onSuccess: () -> Unit) {
        database.stepsDao.addLatestSteps(stepsToAdd)
        onSuccess()
    }

    override fun getStepsDay(key: Long): StepsDay? {
        return database.stepsDao.getStepsDay(key)
    }

    override val getAllStepsDays: LiveData<List<StepsDay>> = database.stepsDao.getAllStepsDays()

    override val getLatestStepsDay: StepsDay = database.stepsDao.getLatestStepsDay()

    override val getLatestStepsDayObservable: LiveData<StepsDay> = database.stepsDao.getLatestStepsDayObservable()

    override suspend fun deleteAllStepsDaysButLatest(onSuccess: () -> Unit) {
        database.stepsDao.deleteAllStepsDaysButLatest()
        onSuccess()
    }

    override suspend fun deleteAllStepsDaysButSeven(onSuccess: () -> Unit) {
        database.stepsDao.deleteAllStepsDaysButSeven()
        onSuccess()
    }

    override suspend fun insertGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        database.stepsDao.insertGoal(goal)
        onSuccess()
    }

    override suspend fun updateGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        database.stepsDao.updateGoal(goal)
        onSuccess()
    }

    override suspend fun deleteGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        database.stepsDao.deleteGoal(goal)
        onSuccess()
    }

    override fun getGoal(key: Long): StepsGoal? {
        return database.stepsDao.getGoal(key)
    }

    override val getAllGoals: LiveData<List<StepsGoal>> = database.stepsDao.getAllGoals()
}