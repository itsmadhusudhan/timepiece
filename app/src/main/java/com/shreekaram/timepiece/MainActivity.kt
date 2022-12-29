package com.shreekaram.timepiece

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.shreekaram.timepiece.presentation.clock.ClockStateViewModel
import com.shreekaram.timepiece.presentation.clock.TimezoneViewModel
import com.shreekaram.timepiece.presentation.clock.UTCTimeModelView
import com.shreekaram.timepiece.presentation.home.RootNavigationGraph
import com.shreekaram.timepiece.ui.theme.TimePieceTheme

val LocalTimezoneViewModel = compositionLocalOf<TimezoneViewModel> {
	error("Timezones are not set")
}
val LocalUTCTimeViewModel = compositionLocalOf<UTCTimeModelView> {
	error("utc date not set")
}
val LocalClockStateViewModel = compositionLocalOf<ClockStateViewModel> {
	error("clock state is not set")
}


class MainActivity : ComponentActivity() {
	private val viewModel: TimezoneViewModel by viewModels()
	private val utcViewModel: UTCTimeModelView by viewModels()
	private val clockStateViewModel: ClockStateViewModel by viewModels()

	@OptIn(ExperimentalAnimationApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen()

		setContent {
			TimePieceTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background,
				) {
					CompositionLocalProvider(
						LocalTimezoneViewModel provides viewModel,
						// FIXME: refactor to localise it
						LocalUTCTimeViewModel provides utcViewModel,
						LocalClockStateViewModel provides clockStateViewModel,
					) {
						val navController = rememberAnimatedNavController()

						RootNavigationGraph(navController = navController)
					}
				}
			}
		}
	}
}

