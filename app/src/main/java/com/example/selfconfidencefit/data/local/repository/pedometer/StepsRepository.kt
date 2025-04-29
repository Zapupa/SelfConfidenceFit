package com.example.selfconfidencefit.data.local.repository.pedometer

import androidx.lifecycle.LiveData
import com.example.selfconfidencefit.data.local.models.pedometer.StepsDay
import com.example.selfconfidencefit.data.local.models.pedometer.StepsGoal
import com.example.selfconfidencefit.data.local.dao.pedometer.StepsDao
import javax.inject.Inject

class StepsRepository @Inject constructor(private val stepsDao: StepsDao) : IStepsRepository {
    override suspend fun insertStepsDay(steps: StepsDay) {
        stepsDao.insertStepsDay(steps)
//        onSuccess()
    }

    override suspend fun updateStepsDay(steps: StepsDay, onSuccess: () -> Unit) {
        stepsDao.updateStepsDay(steps)
        onSuccess()
    }

    override suspend fun addLatestSteps(stepsToAdd: Int, onSuccess: () -> Unit) {
        stepsDao.addLatestSteps(stepsToAdd)
        onSuccess()
    }

    override fun getStepsDay(key: Long): LiveData<StepsDay?> {
        return stepsDao.getStepsDay(key)
    }

    override val getAllStepsDays: LiveData<List<StepsDay>> = stepsDao.getAllStepsDays()

    override val getLatestStepsDay: LiveData<StepsDay> = stepsDao.getLatestStepsDay()

    override val getLatestStepsDayObservable: LiveData<StepsDay> = stepsDao.getLatestStepsDayObservable()

    suspend fun getStepsByDate(date: String): StepsDay? {
        return stepsDao.getStepsByDate(date)
    }

    suspend fun incrementSteps(date: String, increment: Int){
        stepsDao.incrementSteps(date, increment)
    }

    override suspend fun deleteAllStepsDaysButLatest(onSuccess: () -> Unit) {
        stepsDao.deleteAllStepsDaysButLatest()
        onSuccess()
    }

    override suspend fun deleteAllStepsDaysButSeven(onSuccess: () -> Unit) {
        stepsDao.deleteAllStepsDaysButSeven()
        onSuccess()
    }

    override suspend fun insertGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        stepsDao.insertGoal(goal)
        onSuccess()
    }

    override suspend fun updateGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        stepsDao.updateGoal(goal)
        onSuccess()
    }

    override suspend fun deleteGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        stepsDao.deleteGoal(goal)
        onSuccess()
    }

    override fun getGoal(key: Long): LiveData<StepsGoal?> {
        return stepsDao.getGoal(key)
    }

    override val getAllGoals: LiveData<List<StepsGoal>> = stepsDao.getAllGoals()
}