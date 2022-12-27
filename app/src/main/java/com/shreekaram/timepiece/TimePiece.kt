package com.shreekaram.timepiece

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.shreekaram.timepiece.presentation.clock.TimezoneViewModel
import com.shreekaram.timepiece.presentation.home.RootNavigationGraph
import com.shreekaram.timepiece.ui.theme.TimePieceTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePiece(viewModel: TimezoneViewModel) {
	val navController = rememberNavController()

	TimePieceTheme {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colors.background,
		) {
			CompositionLocalProvider(LocalTimezoneViewModel provides viewModel) {
				RootNavigationGraph(navController = navController)
			}
		}
	}
}
