package com.shreekaram.timepiece.data

import android.util.Log
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.repository.TimezoneRepository
import com.shreekaram.timepiece.proto.ClockState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimezoneRepositoryImpl @Inject constructor(private val storage: TimezoneStorage) : TimezoneRepository {
	override suspend fun saveTimezone(timezone: NativeTimezone) {
		val value = storage.insert(timezone)

	}

	override suspend fun getAllTimezones(): Flow<ClockState> {
		return storage.getAll()
	}

	override suspend fun getHomeTimezone(): Flow<NativeTimezone> {
		return storage.getHomeTimezone()
	}

	override suspend fun removeTimezone(key: String) {
		storage.remove(key)
	}

}