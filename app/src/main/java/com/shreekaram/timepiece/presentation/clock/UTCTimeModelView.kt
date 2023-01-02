package com.shreekaram.timepiece.presentation.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import kotlinx.coroutines.launch

class UTCTimeModelView : ViewModel() {
    private val _utcDate = MutableLiveData(OffsetDateTime.now(ZoneOffset.UTC))

    lateinit var timer: Timer

    val utcDate: LiveData<OffsetDateTime>
        get() = _utcDate

    init {
        viewModelScope.launch {
            timer = Timer()

            val tt: TimerTask = object : TimerTask() {
                override fun run() {
                    _utcDate.postValue(OffsetDateTime.now(ZoneOffset.UTC))
                }
            }

            timer.schedule(tt, Date(), 1000)
        }
    }

    override fun onCleared() {
        timer.cancel()
        super.onCleared()
    }
}
