package com.shreekaram.timepiece.presentation.clock

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.NativeTimezoneDeserializer
import com.shreekaram.timepiece.domain.clock.TimeDuration

enum class TimeZoneSort(val value: String) {
	CITY_NAME("Name"),
	TIMEZONE("Time zone")
}

class TimezoneViewModel(application: Application) : AndroidViewModel(application) {
	private var _sortType = MutableLiveData<TimeZoneSort>(TimeZoneSort.CITY_NAME)
	private val timezonesLiveData: LiveData<List<NativeTimezone>> by lazy {
		Log.d("INIT", "values are loading")
		val liveData = MutableLiveData<List<NativeTimezone>>()

		liveData.value = loadTimezones().sortedBy { it.cityName }

		return@lazy liveData
	}

	val sortType: LiveData<TimeZoneSort>
		get() = _sortType

	val timezones: LiveData<List<NativeTimezone>>
		get() = timezonesLiveData

//	init {
//		println(timezones.value)
//		Log.d("INIT", "Timezone view")
//		viewModelScope.launch() {
//			loadTimezones()
//		}
//	}

	fun setSortType(type: TimeZoneSort) {
		_sortType.value = type
	}

	private fun loadTimezones(): List<NativeTimezone> {
		val inputStream = this.getApplication<Application>().assets.open("timezones.json")
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

