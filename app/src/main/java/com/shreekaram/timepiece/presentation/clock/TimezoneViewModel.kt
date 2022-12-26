package com.shreekaram.timepiece.presentation.clock

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.NativeTimezoneDeserializer
import com.shreekaram.timepiece.domain.clock.TimeDuration
import kotlinx.coroutines.launch

enum class TimeZoneSort {
	CITY_NAME,
	TIMEZONE
}

class TimezoneViewModel(application: Application): AndroidViewModel(application) {
	private var _timezones= MutableLiveData<List<NativeTimezone>>(emptyList<NativeTimezone>())
	private var _sortType=MutableLiveData<TimeZoneSort>(TimeZoneSort.CITY_NAME)

	val sortType=MutableLiveData<TimeZoneSort>(TimeZoneSort.CITY_NAME)

	val timezones: LiveData<List<NativeTimezone>>
		get() = _timezones

	init {
		viewModelScope.launch() {
			loadTimezones()
		}
	}

	private fun loadTimezones(){
		val inputStream = this.getApplication<Application>().assets.open("timezones.json")
		val gson = GsonBuilder()
			.registerTypeAdapter(NativeTimezone::class.java, NativeTimezoneDeserializer())
			.create()
		val type = object : TypeToken<List<NativeTimezone>>() {}.type
		val list = gson.fromJson<List<NativeTimezone>>(inputStream.reader(), type)

		_timezones.value = list.map {
			it.cityName = it.zoneName.split("/").last()
			it.duration = TimeDuration.fromSeconds(it.gmtOffset.toLong());

			it
		}.toList()
	}
}

