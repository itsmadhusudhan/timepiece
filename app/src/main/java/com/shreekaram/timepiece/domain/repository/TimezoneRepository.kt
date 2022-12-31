package com.shreekaram.timepiece.domain.repository

import com.shreekaram.timepiece.domain.clock.NativeTimezone
import kotlinx.coroutines.flow.Flow
import com.shreekaram.timepiece.proto.ClockState

interface TimezoneRepository {
	suspend fun saveTimezone(timezone: NativeTimezone):Unit
	suspend fun getAllTimezones():Flow<ClockState>
	suspend fun getHomeTimezone():Flow<NativeTimezone>
	suspend fun removeTimezone(key: String)
}