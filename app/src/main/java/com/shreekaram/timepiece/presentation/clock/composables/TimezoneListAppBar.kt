package com.shreekaram.timepiece.presentation.clock.composables

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ButtonActions{
	BACK_BUTTON,
	SEARCH_BUTTON,
	FILTER_BUTTON
}

@Composable
fun TimezoneListAppBar(
	onClickAction:(action: ButtonActions)->Unit
) {
	val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

	TopAppBar(
		title = { Text("Cities", fontSize = 16.sp) },
		backgroundColor = MaterialTheme.colors.background,
		contentColor = MaterialTheme.colors.onBackground,
		elevation = 0.dp,
		navigationIcon = {
			IconButton(onClick = { onClickAction(ButtonActions.BACK_BUTTON) }) {
				Icon(Icons.Filled.ArrowBack, "Back")
			}
		},
		modifier = Modifier.drawBehind {
			drawLine(
				borderColor,
				Offset(0F, size.height),
				Offset(size.width, size.height),
				1F
			)
		},
		actions = {
			IconButton(onClick = { onClickAction(ButtonActions.SEARCH_BUTTON) }) {
				Icon(Icons.Outlined.Search, "Search Cities")
			}
			IconButton(onClick = { onClickAction(ButtonActions.FILTER_BUTTON) }) {
				Icon(Icons.Filled.Sort, "Sort Cities")
			}
		}
	)
}


