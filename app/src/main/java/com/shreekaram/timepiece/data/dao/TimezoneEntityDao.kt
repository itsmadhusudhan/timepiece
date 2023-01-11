package com.shreekaram.timepiece.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.shreekaram.timepiece.data.entities.TimezoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimezoneEntityDao {
    @Transaction
    @Query("SELECT * FROM timezone")
    fun getAll(): LiveData<List<TimezoneEntity>>

    @Transaction
    @Query("SELECT * from timezone")
    fun getAllTimezones(): Flow<List<TimezoneEntity>>

    @Insert
    suspend fun insert(timezoneEntity: TimezoneEntity)

    @Query("DELETE from timezone where zone_name= :zoneName")
    suspend fun delete(zoneName: String)
}
