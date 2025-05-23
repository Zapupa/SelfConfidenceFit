package com.example.selfconfidencefit.features.pedometer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.selfconfidencefit.data.local.models.pedometer.StepsDay
import com.example.selfconfidencefit.data.local.models.pedometer.StepsGoal
import com.example.selfconfidencefit.data.local.repository.pedometer.StepsRepository
import com.example.selfconfidencefit.utils.DateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(private val repository: StepsRepository) : ViewModel() {
    fun readStepsDay(key: Long) : LiveData<StepsDay?> = repository.getStepsDay(key)

    fun readAllStepsDays() : LiveData<List<StepsDay>> = repository.getAllStepsDays

    fun readLatestStepsDay() : LiveData<StepsDay> = repository.getLatestStepsDay

    fun readLatestStepsDayObservable() : LiveData<StepsDay> = repository.getLatestStepsDayObservable

    fun insertStepsDay(steps: StepsDay) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertStepsDay(steps)
        }
    }

    fun updateStepsDay(steps: StepsDay, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStepsDay(steps) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun addLatestSteps(stepsToAdd: Int, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLatestSteps(stepsToAdd) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun deleteAllStepsDaysButLatest(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllStepsDaysButLatest() {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun deleteAllStepsDaysButSeven(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllStepsDaysButSeven() {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun readGoal(key: Long): LiveData<StepsGoal?> = repository.getGoal(key)

    fun readAllGoals(): LiveData<List<StepsGoal>> = repository.getAllGoals

    fun insertGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertGoal(goal) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun updateGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGoal(goal) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }

    fun deleteGoal(goal: StepsGoal, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGoal(goal) {
                viewModelScope.launch(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }



    val todaySteps: LiveData<Int> = liveData {
        readLatestStepsDayObservable().value?.steps.let {
            if (it != null) {
                emit(it)
            }
            else{
                emit(0)
            }
        }
    }

    val activeGoal: Flow<Int> = flow{
        repository.getGoal(1).value?.goal?.let { emit(it) } ?: emit(10000)
    }

}