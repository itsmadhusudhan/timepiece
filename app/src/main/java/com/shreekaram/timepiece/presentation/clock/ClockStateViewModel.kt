package com.shreekaram.timepiece.presentation.clock

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.repository.ClockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockStateViewModel @Inject constructor(private val repository: ClockRepository) :
	ViewModel() {
	private val initialiseTimezones: () -> MutableLiveData<MutableMap<String, NativeTimezone>> = {
		val liveData:MutableLiveData<MutableMap<String, NativeTimezone>> = MutableLiveData(
			mutableMapOf()
		)
		viewModelScope.launch {
			val zoneMap= repository.getTimezones()
				.first()
				.associateBy { it.zoneName }
				.toMutableMap()

			liveData.postValue(zoneMap)
		}

		liveData
	}

	private val initialiseHomeTimezone:()-> MutableLiveData<NativeTimezone> = {
		val liveData:MutableLiveData<NativeTimezone> = MutableLiveData(NativeTimezone.current())

		viewModelScope.launch {
			val timezone= repository.getHomeTimezone().first()

			println(timezone)

			liveData.postValue(timezone)
		}

		 liveData
	}

	private val initialiseTimezoneSort:()-> MutableLiveData<TimeZoneSort> = {
		val liveData:MutableLiveData<TimeZoneSort> = MutableLiveData(TimeZoneSort.TIMEZONE)

		viewModelScope.launch {
			val sortType= repository.getTimezoneSort().first()

//			val sort= when(sortType){
//				TimezoneSort.TIMEZONE->TimeZoneSort.TIMEZONE
//				else -> TimeZoneSort.CITY_NAME
//			}
//
			liveData.postValue(sortType)
		}

		liveData
	}

	private val _currentTimezone = MutableLiveData(NativeTimezone.current())
	private val _timezones: MutableLiveData<MutableMap<String, NativeTimezone>> by lazy(initializer = initialiseTimezones)
	private  val _homeTimezone: MutableLiveData<NativeTimezone> by lazy(initializer = initialiseHomeTimezone)
	private  val _sortType: MutableLiveData<TimeZoneSort> by lazy(initializer = initialiseTimezoneSort)

	val currentTimezone: LiveData<NativeTimezone> get() = _currentTimezone
	val homeTimezone: LiveData<NativeTimezone> get() = _homeTimezone
	val timezones: LiveData<MutableMap<String, NativeTimezone>>
		get() = _timezones
	val sortType: LiveData<TimeZoneSort>
		get() = _sortType

	fun updateSortType(type: TimeZoneSort){
//		val sortType= when(type){
//			TimeZoneSort.TIMEZONE -> TimezoneSort.TIMEZONE
//			TimeZoneSort.CITY_NAME -> TimezoneSort.CITY_NAME
//		}

		viewModelScope.launch {
			repository.updateSort(type)

			_sortType.postValue(type)

		}

	}

	fun updateHomeTimezone(timezone: NativeTimezone) {
		println("Update home")
		viewModelScope.launch {
			repository.updateHomeTimezone(timezone)

			_homeTimezone.postValue(timezone)
		}

	}

	fun addTimezone(timezone: NativeTimezone) {
		viewModelScope.launch {
			val value=	repository.saveTimezone(timezone).first()
			Log.d("SAVE",value.toString())
		}

		_timezones.postValue(
			_timezones.value!!.plus(Pair(timezone.zoneName, timezone)).toMutableMap()
		)
	}

	fun removeTimezone(key: String) {
		viewModelScope.launch {
			repository.removeTimezone(key)
		}

		_timezones.value!!.remove(key)
	}

	fun containsZone(key: String): Boolean {
		return _timezones.value!!.contains(key)
	}

	val size: Int
		get() = _timezones.value!!.size
}

//
//	private val _homeTimezone: MutableLiveData<NativeTimezone> by lazy {
//		val liveData = MutableLiveData(NativeTimezone.current())
//
//		viewModelScope.launch {
////			liveData.postValue(repository.getHomeTimezone().first())
//		}
//
//		return@lazy liveData
//	}

//	private val _timezones: MutableLiveData<MutableMap<String,NativeTimezone>> by lazy{
//		val liveData = MutableLiveData<MutableMap<String,NativeTimezone>>(mutableMapOf())
//
//		viewModelScope.launch {
//			val zonesMap = loadTimezones().await()
//
//			liveData.postValue(zonesMap)
//		}
//
//		return@lazy liveData
//	}