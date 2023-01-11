package com.shreekaram.timepiece.domain.repository

import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.presentation.clock.TimeZoneSort
import kotlinx.coroutines.flow.Flow

interface ClockRepository {
    suspend fun getTimezones(): Flow<List<NativeTimezone>>
    suspend fun getHomeTimezone(): Flow<NativeTimezone>
    suspend fun getTimezoneSort(): Flow<TimeZoneSort>
    suspend fun saveTimezone(timezone: NativeTimezone)
    suspend fun removeTimezone(zoneName: String)
    suspend fun updateHomeTimezone(timezone: NativeTimezone)
    suspend fun updateSort(sortType: TimeZoneSort)
}
