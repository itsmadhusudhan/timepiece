package com.shreekaram.timepiece.data

import androidx.datastore.core.DataStore
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.toTimezoneModel
import javax.inject.Inject
import com.shreekaram.timepiece.domain.data.Result
import com.shreekaram.timepiece.proto.ClockState
import kotlinx.coroutines.flow.*


class TimezoneStorage @Inject constructor(private val store: DataStore<ClockState>) {
	 suspend fun insert(timezone: NativeTimezone) {
		store.updateData { state ->
			state.toBuilder().addTimezones(timezone.toTimezoneModel()).build()
		}
	}

	suspend fun remove(key:String){
		store.updateData {
			val index=it.timezonesList.indexOfFirst { it.zoneName==key }

			if(index>=0) {
				it.toBuilder()
					.removeTimezones(index)
					.build()
			}

			it
		}
	}


	 suspend fun insert(data: List<NativeTimezone>): Flow<Result> {
		TODO("Not yet implemented")
	}

	 suspend fun getHomeTimezone(): Flow<NativeTimezone> {
		 return flow {
			 val timezone=store.data.first().homeTimezone

			 val homeTimezone= NativeTimezone.fromTimezoneModel(timezone)

			 emit(homeTimezone)
		 }
	}

	 suspend fun getAll(): Flow<ClockState> {
		return flow{ emit(store.data.first())  }
	 }

	 suspend fun clearAll(): Flow<Int> {
		TODO("Not yet implemented")
	}
}