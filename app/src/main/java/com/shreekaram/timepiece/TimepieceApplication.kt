package com.shreekaram.timepiece

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TimepieceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
