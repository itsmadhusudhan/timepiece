package com.shreekaram.timepiece.domain.repository

import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.presentation.clock.TimeZoneSort
import com.shreekaram.timepiece.proto.ClockState
import kotlinx.coroutines.flow.Flow

interface ClockRepository {
	suspend fun readClockState(): Flow<ClockState>
	suspend fun getTimezones(): Flow<List<NativeTimezone>>
	suspend fun getHomeTimezone(): Flow<NativeTimezone>
	suspend fun getTimezoneSort(): Flow<TimeZoneSort>
	suspend fun saveTimezone(timezone: NativeTimezone): Flow<Boolean>
	suspend fun removeTimezone(zoneName: String): Flow<Boolean>
	suspend fun updateHomeTimezone(timezone: NativeTimezone): Flow<Boolean>
	suspend fun updateSort(sortType: TimeZoneSort):Flow<Boolean>
}