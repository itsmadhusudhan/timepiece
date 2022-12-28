package com.shreekaram.timepiece.presentation.clock.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.LocalTimezoneViewModel
import com.shreekaram.timepiece.presentation.clock.TimeZoneSort
import com.shreekaram.timepiece.presentation.home.Route
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(bottomSheetState: ModalBottomSheetState) {
	val viewModel= LocalTimezoneViewModel.current

	val sortType = viewModel.sortType.observeAsState().value
	val scope = rememberCoroutineScope()

	fun onClick(option: TimeZoneSort) {
		viewModel.setSortType(option)
		scope.launch {
			bottomSheetState.hide()
		}
	}

	Surface(
		modifier = Modifier
			.height(200.dp)
			.padding(top = 24.dp),
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.Start
		) {
			Row(
				horizontalArrangement = Arrangement.Center,
				modifier = Modifier.fillMaxWidth()
			) {
				Text(
					"Sort Cities By",
					style = TextStyle(
						fontWeight = FontWeight.Medium,
						fontSize = 16.sp
					)
				)
			}
			Spacer(
				modifier = Modifier
					.height(16.dp)
					.fillMaxWidth()
			)

			LazyColumn(){
				items(TimeZoneSort.values()){option->
					Row(
						modifier = Modifier
							.selectable(
								selected = sortType == option,
								onClick = {
									onClick(option)
								},
								role = Role.RadioButton
							)
							.fillMaxWidth()
							.wrapContentHeight()
							.padding(all = 20.dp)
							.size(20.dp),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceBetween

					) {
						Text(option.value)
						RadioButton(
							selected = sortType == option,
							modifier = Modifier.padding(all = Dp(value = 0F)),
							onClick = {
								onClick(option)
							},
							colors = RadioButtonDefaults.colors(
								selectedColor = MaterialTheme.colors.primary
							)
						)
					}
				}
			}

		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeZoneTypeSheet(bottomSheetState: ModalBottomSheetState) {
	ModalBottomSheetLayout(
		sheetState = bottomSheetState,
		modifier = Modifier
			.fillMaxSize()
			.safeContentPadding(),
		sheetContent = {
			BottomSheetContent(bottomSheetState)
		},
		scrimColor = Color.Black.copy(alpha = 0.25f),
		sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
	) {
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimezoneListAppBar(
	navController: NavHostController,
	bottomSheetState: ModalBottomSheetState
) {
	val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

	val scope = rememberCoroutineScope()

	TopAppBar(
		title = { Text("Cities", fontSize = 16.sp) },
		backgroundColor = MaterialTheme.colors.background,
		contentColor = MaterialTheme.colors.onBackground,
		elevation = 0.dp,
		navigationIcon = {
			IconButton(
				onClick = {
					navController.popBackStack()
				}
			) {
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
					navController.navigate(Route.TimezoneSearch.id)
				}
			) {
				Icon(Icons.Outlined.Search, "Search Cities")
			}
			IconButton(
				onClick = {
					scope.launch {
						bottomSheetState.show()
					}
				}
			) {
				Icon(Icons.Filled.Sort, "Sort Cities")
			}
		}
	)
}


