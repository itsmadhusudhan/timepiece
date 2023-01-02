package com.shreekaram.timepiece.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.shreekaram.timepiece.data.dao.TimezoneEntityDao
import com.shreekaram.timepiece.data.entities.TimezoneEntity
import com.shreekaram.timepiece.data.storage.AppDatabase
import java.io.IOException
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TimezoneEntityDaoTest {
    lateinit var database: AppDatabase
    lateinit var timezoneDao: TimezoneEntityDao

    @Before
    fun setupDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        timezoneDao = database.timezoneDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @Test
    @Throws(Exception::class)
    fun insertTimezone() = runTest {
        // 	Arrange
        val timezone = TimezoneEntity(
            zoneName = "Asia/Kolkata",
            abbreviation = "IST",
            isDst = false,
            gmtOffset = 12345F
        )

        // Act
        timezoneDao.insert(timezone)

        // Assert
        GlobalScope.launch(Dispatchers.Main) {
            val results = timezoneDao.getAll()
            results.observeForever {
                assertThat(it.first(), equalTo(timezone))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @Test
    @Throws(Exception::class)
    fun deleteTimezone() = runTest {
        // Arrange
        val timezone = TimezoneEntity(
            zoneName = "Asia/Kolkata",
            abbreviation = "IST",
            isDst = false,
            gmtOffset = 12345F
        )

        // Act
        timezoneDao.insert(timezone)
        timezoneDao.insert(timezone.copy(zoneName = "Asia/Singapore", abbreviation = "SGT"))
        timezoneDao.delete(timezone.zoneName)

        // Assert

        GlobalScope.launch(Dispatchers.Main) {
            val results = timezoneDao.getAll()

            results.observeForever {
                assertThat(it.size, equalTo(1))
                assertThat(it.first(), Matchers.not(timezone))
            }
        }
    }
}
