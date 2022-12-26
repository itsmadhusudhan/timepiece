package com.shreekaram.timepiece.presentation.clock.composables

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.presentation.clock.TimeZoneSort
import com.shreekaram.timepiece.presentation.clock.TimezoneViewModel


@Composable
fun TimezoneListAppBar(navController: NavHostController) {
	val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

	val viewModel: TimezoneViewModel = viewModel()

	var sortType = viewModel.sortType.observeAsState().value

	TopAppBar(
		title = { Text("Cities") },
		backgroundColor = MaterialTheme.colors.background,
		contentColor = MaterialTheme.colors.onBackground,
		elevation = 0.dp,
		navigationIcon = {
			IconButton(onClick = {
				navController.popBackStack()
			}) {
				Icon(Icons.Filled.ArrowBack, "Back")
			}
		},
		modifier = Modifier.drawBehind {
			drawLine(
				borderColor, Offset(0F, size.height), Offset(size.width, size.height), 1F
			)
		},
		actions = {
			IconButton(
				onClick = {
//					TODO: navigate to search screen
				}
			) {
				Icon(Icons.Filled.Search, "Search Cities")
			}

			IconButton(
				onClick = {
					if (sortType == TimeZoneSort.TIMEZONE)
						viewModel.sortType.value = TimeZoneSort.CITY_NAME
					else
						viewModel.sortType.value = TimeZoneSort.TIMEZONE
				}
			) {
				Icon(Icons.Filled.Sort, "Sort Cities")
			}
		}
	)
}