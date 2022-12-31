package com.shreekaram.timepiece.di

import com.shreekaram.timepiece.data.TimezoneRepositoryImpl
import com.shreekaram.timepiece.domain.repository.TimezoneRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimezoneModule {

	@Binds
	@Singleton
	abstract fun bindTimezoneRepository(repository: TimezoneRepositoryImpl):TimezoneRepository
}