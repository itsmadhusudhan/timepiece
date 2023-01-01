package com.shreekaram.timepiece.domain.repository

import androidx.lifecycle.LiveData
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.presentation.clock.TimeZoneSort

interface ClockRepository {
	suspend fun getTimezones(): LiveData<List<NativeTimezone>>
	suspend fun getHomeTimezone(): LiveData<NativeTimezone>
	suspend fun getTimezoneSort(): LiveData<TimeZoneSort>
	suspend fun saveTimezone(timezone: NativeTimezone)
	suspend fun removeTimezone(zoneName: String)
	suspend fun updateHomeTimezone(timezone: NativeTimezone)
	suspend fun updateSort(sortType: TimeZoneSort)
}