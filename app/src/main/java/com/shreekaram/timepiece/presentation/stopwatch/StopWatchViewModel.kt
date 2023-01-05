package com.shreekaram.timepiece.presentation.stopwatch

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shreekaram.timepiece.service.stopwatch.StopWatchManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class StopWatchViewModel @Inject constructor(manager: StopWatchManager) : ViewModel() {
    val state = manager.stopWatchState
    val duration = manager.duration

    private val _lapseList = MutableLiveData<MutableList<Duration>>(mutableStateListOf())
    val lapseList: LiveData<MutableList<Duration>>
        get() = _lapseList

    fun lapse() {
        _lapseList.value!!.add(duration.value!!)
    }
}
