package com.shreekaram.timepiece.presentation.clock

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.NativeTimezoneDeserializer
import com.shreekaram.timepiece.domain.clock.TimeDuration
import com.shreekaram.timepiece.domain.repository.TimezoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class TimeZoneSort(val value: String) {
	CITY_NAME("Name"),
	TIMEZONE("Time zone")
}

@HiltViewModel
class TimezoneViewModel @Inject constructor(private val appContext: Application, private val repository: TimezoneRepository) : ViewModel() {
	private var _sortType = MutableLiveData(TimeZoneSort.CITY_NAME)
	private val timezonesLiveData: LiveData<List<NativeTimezone>> by lazy {
		val liveData = MutableLiveData<List<NativeTimezone>>()

		viewModelScope.launch {
			liveData.value = loadTimezones().sortedBy { it.cityName }
		}

		return@lazy liveData
	}

	val sortType: LiveData<TimeZoneSort>
		get() = _sortType

	val timezones: LiveData<List<NativeTimezone>>
		get() = timezonesLiveData

	private fun loadTimezones(): List<NativeTimezone> {
		val inputStream = appContext.assets.open("timezones.json")
		val gson = GsonBuilder()
			.registerTypeAdapter(NativeTimezone::class.java, NativeTimezoneDeserializer())
			.create()
		val type = object : TypeToken<List<NativeTimezone>>() {}.type
		val list = gson.fromJson<List<NativeTimezone>>(inputStream.reader(), type)

		val values = list.map {
			it.cityName = it.zoneName.split("/").last().split("_").joinToString(" ")
			it.duration = TimeDuration.fromSeconds(it.gmtOffset.toLong())

			it
		}.sortedBy { it.gmtOffset }

		inputStream.close()

		return values
	}
}

