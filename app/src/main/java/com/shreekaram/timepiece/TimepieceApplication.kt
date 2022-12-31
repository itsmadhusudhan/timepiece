package com.shreekaram.timepiece

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class TimepieceApplication:Application() {

	override fun onCreate() {
		super.onCreate()
	}
}