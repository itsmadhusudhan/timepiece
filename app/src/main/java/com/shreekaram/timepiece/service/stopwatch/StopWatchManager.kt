package com.shreekaram.timepiece.service.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import kotlin.time.Duration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopWatchManager @Inject constructor() {
    private var _stopWatchState = MutableLiveData(StopWatchState.IDLE)
    val stopWatchState: LiveData<StopWatchState>
        get() = _stopWatchState

    private val _state = MutableStateFlow(StopWatchState.IDLE)
    val state: StateFlow<StopWatchState>
        get() = _state

    private var _duration = MutableLiveData(Duration.ZERO)
    val duration: LiveData<Duration>
        get() = _duration

    fun updateState(state: StopWatchState) {
        _stopWatchState.postValue(state)
        _state.value = state
    }

    fun updateDuration(value: Duration) {
        _duration.postValue(value)
    }
}
