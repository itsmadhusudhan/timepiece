package com.shreekaram.timepiece.data

import android.util.Log
import androidx.datastore.core.DataStore
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.toTimezoneModel
import com.shreekaram.timepiece.domain.repository.ClockRepository
import com.shreekaram.timepiece.presentation.clock.TimeZoneSort
import com.shreekaram.timepiece.proto.ClockState
import com.shreekaram.timepiece.proto.copy
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class ClockRepositoryImpl @Inject constructor(private val dataStore: DataStore<ClockState>) : ClockRepository {
	override suspend fun readClockState(): Flow<ClockState> {
		return flow { emit(dataStore.data.first()) }
	}

	override suspend fun getTimezones(): Flow<List<NativeTimezone>> {
		return dataStore.data.catch { it.printStackTrace() }.map { it.timezonesList.map { t -> NativeTimezone.fromTimezoneModel(t) }  }
	}

	override suspend fun getHomeTimezone(): Flow<NativeTimezone> {
		return dataStore.data.catch { it.printStackTrace() }.map { NativeTimezone.fromTimezoneModel(it.homeTimezone) }
	}

	override suspend fun getTimezoneSort(): Flow<TimeZoneSort> {
//		return flow {
//			val value=dataStore.data.first().sortType;
//
//			val type= when(value){
//				ClockState.ClockSort.TYPE_TIMEZONE->TimeZoneSort.TIMEZONE
//				ClockState.ClockSort.TYPE_CITY_NAME->TimeZoneSort.CITY_NAME
//				else -> TimeZoneSort.TIMEZONE
//			}
//
//			emit(type)
//		}
//		FIXME:
		return flow { emit(TimeZoneSort.TIMEZONE) }
	}

	override suspend fun saveTimezone(timezone: NativeTimezone): Flow<Boolean> {
		return flow {
			dataStore.updateData {
				it.toBuilder().addTimezones(timezone.toTimezoneModel()).build()
			}

			emit(true)
		}.catch {
			it.printStackTrace()
			emit(false)
		}
	}

	override suspend fun removeTimezone(zoneName: String): Flow<Boolean> {
		return flow {
			dataStore.updateData {
				val index = it.timezonesList.indexOfFirst { t -> t.zoneName == zoneName }

				if (index >= 0) {
					return@updateData it.toBuilder().removeTimezones(index).build()
				} else {
					return@updateData it
				}
			}

			emit(true)
		}.catch {
			it.printStackTrace()
			emit(false)
		}
	}

	override suspend fun updateHomeTimezone(timezone: NativeTimezone): Flow<Boolean> {
		return flow {
			dataStore.updateData {
				it.toBuilder().setHomeTimezone(timezone.toTimezoneModel()).build()
			}

			emit(true)
		}.catch {
			it.printStackTrace()
			emit(false)
		}
	}

	override suspend fun updateSort(sortType: TimeZoneSort): Flow<Boolean> {
//		Log.d("SAVE","Saving values")
//		println(sortType)
//
//		val type= when(sortType){
//			TimeZoneSort.TIMEZONE -> ClockState.ClockSort.TYPE_TIMEZONE
//			TimeZoneSort.CITY_NAME -> ClockState.ClockSort.TYPE_CITY_NAME
//		}
//
//		Log.d("SORT",type.name)
//
//		return flow {
//			dataStore.updateData {
//				it.toBuilder().setSortType(type).build()
//			}
//			emit(true)
//		}.catch {
//			it.printStackTrace()
//			emit(false)
//		}

		return flow { emit(true) }
	}
}