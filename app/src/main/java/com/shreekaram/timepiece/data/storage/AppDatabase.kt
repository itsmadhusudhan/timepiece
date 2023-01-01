package com.shreekaram.timepiece.data.storage

import android.content.Context
import androidx.room.*
import com.shreekaram.timepiece.data.dao.TimezoneEntityDao
import com.shreekaram.timepiece.data.entities.TimezoneEntity

private const val DATABASE_NAME="timepiece.db"

@Database(entities = [TimezoneEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
	abstract fun timezoneDao(): TimezoneEntityDao

	companion object{
		@Volatile
		private var instance: AppDatabase?=null


		fun getInstance(context: Context): AppDatabase {
			return instance ?: synchronized(this){
				instance ?: buildDatabase(context).also { instance = it }
			}
		}

		private fun  buildDatabase(context: Context): AppDatabase{
			return Room.databaseBuilder(context, AppDatabase::class.java,DATABASE_NAME)
				.addCallback(
					object : Callback(){
//						override fun onCreate(db: SupportSQLiteDatabase) {
//							super.onCreate(db)
//							// NOTE: seed data using workers here
//						}
					}
				).build()

		}
	}
}