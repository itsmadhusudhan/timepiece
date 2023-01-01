package com.shreekaram.timepiece.data.repository

import androidx.datastore.core.DataStore
import androidx.lifecycle.*
import com.shreekaram.timepiece.data.dao.TimezoneEntityDao
import com.shreekaram.timepiece.data.entities.TimezoneEntity
import com.shreekaram.timepiece.data.entities.toDomain
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.toTimezoneModel
import com.shreekaram.timepiece.domain.repository.ClockRepository
import com.shreekaram.timepiece.presentation.clock.TimeZoneSort
import com.shreekaram.timepiece.proto.ClockState
import javax.inject.Inject


class ClockRepositoryImpl @Inject constructor(private val dataStore: DataStore<ClockState>, private val timezoneDao: TimezoneEntityDao) : ClockRepository {
	private val storeLiveData=dataStore.data.asLiveData()

	override suspend fun getTimezones(): LiveData<List<NativeTimezone>> {
		return	Transformations.distinctUntilChanged(
			Transformations.map(timezoneDao.getAll()){ it.map { t -> t.toDomain() }}
		)
	}


	override suspend fun getHomeTimezone(): LiveData<NativeTimezone> {
		return Transformations.map(dataStore.data.asLiveData()){ NativeTimezone.fromTimezoneModel(it.homeTimezone) }
	}

	override suspend fun getTimezoneSort(): LiveData<TimeZoneSort> {
		return Transformations.map(storeLiveData){
			when(it.sortType){
				ClockState.ClockSort.TYPE_TIMEZONE -> TimeZoneSort.TIMEZONE
				else -> TimeZoneSort.CITY_NAME
			}
		}
	}

	override suspend fun saveTimezone(timezone: NativeTimezone) {
		return timezoneDao.insert(TimezoneEntity.fromDomain(timezone))
	}

	override suspend fun removeTimezone(zoneName: String){
		return timezoneDao.delete(zoneName)
	}

	override suspend fun updateHomeTimezone(timezone: NativeTimezone){
		dataStore.updateData {
			it.toBuilder().setHomeTimezone(timezone.toTimezoneModel()).build()
		}
	}

	override suspend fun updateSort(sortType: TimeZoneSort) {
		val type= when(sortType){
			TimeZoneSort.TIMEZONE -> ClockState.ClockSort.TYPE_TIMEZONE
			TimeZoneSort.CITY_NAME -> ClockState.ClockSort.TYPE_CITY_NAME
		}

		dataStore.updateData {
			it.toBuilder().setSortType(type).build()
		}
	}
}