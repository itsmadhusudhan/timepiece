package com.shreekaram.timepiece

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.shreekaram.timepiece.presentation.clock.TimezoneViewModel
import com.shreekaram.timepiece.presentation.clock.UTCTimeModelView
import com.shreekaram.timepiece.presentation.home.RootNavigationGraph
import com.shreekaram.timepiece.ui.theme.TimePieceTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimePiece(viewModel: TimezoneViewModel) {
	val navController = rememberAnimatedNavController()

	TimePieceTheme {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colors.background,
		) {
			CompositionLocalProvider(
				LocalTimezoneViewModel provides viewModel,
//				FIXME: refactor to localise it
				LocalUTCTimeViewModel provides  UTCTimeModelView()
			) {
				RootNavigationGraph(navController = navController)
			}
		}
	}
}
