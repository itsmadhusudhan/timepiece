package com.shreekaram.timepiece

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.shreekaram.timepiece.presentation.clock.TimezoneViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TimezoneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        viewModel.timezones.observe(this){
            Log.d("Viewmodels",it.size.toString())
        }

        setContent {
           TimePiece()
        }
    }
}

