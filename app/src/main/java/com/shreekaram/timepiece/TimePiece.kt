package com.shreekaram.timepiece

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.shreekaram.timepiece.presentation.clock.TimezoneViewModel
import com.shreekaram.timepiece.presentation.clock.UTCTimeModelView
import com.shreekaram.timepiece.presentation.home.RootNavigationGraph
import com.shreekaram.timepiece.ui.theme.TimePieceTheme

@Composable
fun TimePiece(viewModel: TimezoneViewModel) {
	val navController = rememberNavController()

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
