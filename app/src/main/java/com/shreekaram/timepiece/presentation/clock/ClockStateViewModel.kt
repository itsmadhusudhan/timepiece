package com.shreekaram.timepiece.presentation.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import java.util.*


class ClockStateViewModel : ViewModel() {
	private val _currentTimezone: MutableLiveData<NativeTimezone> by lazy {
		val _timezone = TimeZone.getDefault()

		val liveData = MutableLiveData(NativeTimezone.fromTimezone(_timezone))

		return@lazy liveData
	}
	private val _timezones = MutableLiveData<MutableMap<String,NativeTimezone>>(mutableMapOf())

	val currentTimezone: LiveData<NativeTimezone> get() = _currentTimezone
	val timezones: LiveData<MutableMap<String,NativeTimezone>> get() =_timezones

	fun updateCurrentTimezone(timezone: NativeTimezone) {
		_currentTimezone.postValue(timezone)
	}

	fun addTimezone(timezone: NativeTimezone) {
		_timezones.postValue(_timezones.value!!.plus(Pair(timezone.zoneName,timezone)).toMutableMap())
	}

	fun removeTimezone(key: String) {
		_timezones.value!!.remove(key)
	}

	fun containsZone(key:String):Boolean{
		return _timezones.value!!.contains(key)
	}

	val size:Int
		get() = _timezones.value!!.size
}