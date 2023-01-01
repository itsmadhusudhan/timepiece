package com.shreekaram.timepiece.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.shreekaram.timepiece.data.proto.ClockStateSerializer
import com.shreekaram.timepiece.proto.ClockState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

const val clockStatePreferenceFile="clock_state.pb"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
	@Provides
	@Singleton
	fun provideClockDataStore(@ApplicationContext appContext: Context): DataStore<ClockState>{
		return	DataStoreFactory.create(
			serializer = ClockStateSerializer,
			produceFile = {
				appContext.dataStoreFile(clockStatePreferenceFile)
			},
		)
	}
}
