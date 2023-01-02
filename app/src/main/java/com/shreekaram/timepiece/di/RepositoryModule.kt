package com.shreekaram.timepiece.di

import com.shreekaram.timepiece.data.repository.ClockRepositoryImpl
import com.shreekaram.timepiece.domain.repository.ClockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindClockRepository(repository: ClockRepositoryImpl): ClockRepository
}
