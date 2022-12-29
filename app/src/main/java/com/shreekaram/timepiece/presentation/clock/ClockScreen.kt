package com.shreekaram.timepiece.presentation.clock

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.LocalClockStateViewModel
import com.shreekaram.timepiece.LocalUTCTimeViewModel
import com.shreekaram.timepiece.presentation.home.Route
import java.time.format.DateTimeFormatter
import java.util.*


const val maxHeight = 150
const val minHeight = 60

val fmt: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mma, E, M/dd")
val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale("en"))
val dateFormatter = DateTimeFormatter.ofPattern("E, M/dd", Locale("en"))

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ClockScreen(navController: NavHostController) {

	Scaffold(
		floatingActionButton = {
			FloatingActionButton(
				modifier = Modifier.padding(bottom = 60.dp),
				onClick = {
					navController.navigate(Route.TimezoneList.id)
				},
				backgroundColor = MaterialTheme.colors.primary,
			) {
				Icon(Icons.Filled.Language, "Timezones")
			}
		},
		floatingActionButtonPosition = FabPosition.Center,
		isFloatingActionButtonDocked = true,
	) {

		val density = LocalDensity.current.density
		val headerHeightPx = with(LocalDensity.current) {
			maxHeight.dp.roundToPx().toFloat()
		}
		val headerMinHeightPx = with(LocalDensity.current) {
			minHeight.dp.roundToPx().toFloat()
		}
		val headerOffsetHeightPx = remember {
			mutableStateOf(0F)
		}
		val nestedScrollConnection = remember {
			object : NestedScrollConnection {
				override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
					val delta = available.y
					val newOffset = headerOffsetHeightPx.value + delta

					headerOffsetHeightPx.value =
						newOffset.coerceIn(headerMinHeightPx - headerHeightPx, 0F)

					return Offset.Zero
				}
			}
		}

		var progress by remember { mutableStateOf(0F) }

		LaunchedEffect(key1 = headerOffsetHeightPx.value) {
			progress =
				((headerHeightPx + headerOffsetHeightPx.value) / headerHeightPx - minHeight / maxHeight) / (1f - minHeight / maxHeight)
		}

		Column(
			modifier = Modifier
				.fillMaxSize()
				.nestedScroll(nestedScrollConnection)
				.padding(bottom = it.calculateBottomPadding())
		) {
			Header(
				height = ((headerHeightPx + headerOffsetHeightPx.value) / density).dp,
				progress = progress,
				onClick = { navController.navigate(Route.Settings.id) }
			)
			ClockListView()
		}
	}
}

@Composable
fun Header(height: Dp, progress: Float, onClick: () -> Unit) {
	val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(height)
			.background(MaterialTheme.colors.surface)
			.drawBehind {
				if (progress < 0.75)
					drawLine(
						borderColor,
						Offset(0F, size.height),
						Offset(size.width, size.height),
						1F
					)
			}
			.padding(horizontal = 20.dp, vertical = 4.dp)
	) {
		Column(
			modifier = Modifier.align(Alignment.BottomStart)
		) {
			Text(
				text = "World Clock",
				style = MaterialTheme.typography.h3.copy(
					fontSize = (28 * progress + 8).sp
				)
			)
			Spacer(Modifier.height(0.dp))
			Today()
		}
		IconButton(
			onClick = onClick,
			modifier = Modifier
				.align(Alignment.TopEnd)
				.padding(all = 0.dp)
		) {
			Icon(Icons.Outlined.Settings, "settings")
		}
	}
}

@Composable
fun Today() {
	val currentTimezone = LocalClockStateViewModel.current.currentTimezone.observeAsState().value!!

	val currentDate =
		LocalUTCTimeViewModel.current.utcDate.observeAsState().value!!
			.plusHours(currentTimezone.duration.hour)
			.plusMinutes(currentTimezone.duration.minutes)

	Text(
		text = "${currentTimezone.cityName}, ${fmt.format(currentDate)}",
		color = Color.Gray,
		style = MaterialTheme.typography.caption
	)
}
