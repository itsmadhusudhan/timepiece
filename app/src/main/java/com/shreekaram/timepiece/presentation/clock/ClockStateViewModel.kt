package com.shreekaram.timepiece.presentation.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.repository.ClockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ClockStateViewModel @Inject constructor(private val repository: ClockRepository) :
    ViewModel() {
    private val initialiseHomeTimezone: () -> MutableLiveData<NativeTimezone> = {
        val liveData: MutableLiveData<NativeTimezone> = MutableLiveData(NativeTimezone.current())

        viewModelScope.launch {
            val timezone = repository.getHomeTimezone().value

            liveData.postValue(timezone)
        }

        liveData
    }

    private val initialiseTimezoneSort: () -> MutableLiveData<TimeZoneSort> = {
        val liveData: MutableLiveData<TimeZoneSort> = MutableLiveData(TimeZoneSort.TIMEZONE)

        viewModelScope.launch {
            val sortType = repository.getTimezoneSort().value

            liveData.postValue(sortType)
        }

        liveData
    }

    private val _currentTimezone = MutableLiveData(NativeTimezone.current())
    private val _timezones: MutableLiveData<MutableMap<String, NativeTimezone>> = MutableLiveData(mutableMapOf())
    private val _homeTimezone: MutableLiveData<NativeTimezone> by lazy(initializer = initialiseHomeTimezone)
    private val _sortType: MutableLiveData<TimeZoneSort> by lazy(initializer = initialiseTimezoneSort)

    val currentTimezone: LiveData<NativeTimezone> get() = _currentTimezone
    val homeTimezone: LiveData<NativeTimezone> get() = _homeTimezone
    val timezones: LiveData<MutableMap<String, NativeTimezone>>
        get() = _timezones
    val sortType: LiveData<TimeZoneSort>
        get() = _sortType

    init {
        viewModelScope.launch {
            repository.getTimezones()
                .observeForever { list ->
                    _timezones.postValue(list.associateBy { it.zoneName }.toMutableMap())
                }

            repository.getHomeTimezone().observeForever {
                _homeTimezone.postValue(it)
            }

            repository.getTimezoneSort().observeForever {
                _sortType.postValue(it)
            }
        }
    }

    fun updateSortType(type: TimeZoneSort) {
        viewModelScope.launch {
            repository.updateSort(type)

            _sortType.postValue(type)
        }
    }

    fun updateHomeTimezone(timezone: NativeTimezone) {
        viewModelScope.launch {
            repository.updateHomeTimezone(timezone)

// 			_homeTimezone.postValue(timezone)
        }
    }

    fun addTimezone(timezone: NativeTimezone) {
        viewModelScope.launch { repository.saveTimezone(timezone) }
    }

    fun removeTimezone(key: String) {
        viewModelScope.launch { repository.removeTimezone(key) }
    }

    val containsZone: (key: String) -> Boolean = {
        _timezones.value!!.contains(it)
    }

    val size: Int
        get() = _timezones.value!!.size
}

//
// 	private val _homeTimezone: MutableLiveData<NativeTimezone> by lazy {
// 		val liveData = MutableLiveData(NativeTimezone.current())
//
// 		viewModelScope.launch {
// //			liveData.postValue(repository.getHomeTimezone().first())
// 		}
//
// 		return@lazy liveData
// 	}

// 	private val _timezones: MutableLiveData<MutableMap<String,NativeTimezone>> by lazy{
// 		val liveData = MutableLiveData<MutableMap<String,NativeTimezone>>(mutableMapOf())
//
// 		viewModelScope.launch {
// 			val zonesMap = loadTimezones().await()
//
// 			liveData.postValue(zonesMap)
// 		}
//
// 		return@lazy liveData
// 	}
