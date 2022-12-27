package com.shreekaram.timepiece

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.compositionLocalOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.shreekaram.timepiece.presentation.clock.TimezoneViewModel

val LocalTimezoneViewModel = compositionLocalOf<TimezoneViewModel> { error("No user found!") }

class MainActivity : ComponentActivity() {
	private val viewModel: TimezoneViewModel by viewModels()

	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen()

		setContent {
			TimePiece(viewModel)
		}
	}
}

