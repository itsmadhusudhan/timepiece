package com.shreekaram.timepiece.presentation.clock

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.LocalClockStateViewModel
import com.shreekaram.timepiece.LocalTimezoneViewModel
import com.shreekaram.timepiece.LocalUTCTimeViewModel
import com.shreekaram.timepiece.domain.clock.*
import com.shreekaram.timepiece.presentation.clock.composables.ButtonActions
import com.shreekaram.timepiece.presentation.clock.composables.CustomCheckbox
import com.shreekaram.timepiece.presentation.clock.composables.TimezoneFilterModal
import com.shreekaram.timepiece.presentation.clock.composables.TimezoneListAppBar
import com.shreekaram.timepiece.presentation.home.Route
import kotlinx.coroutines.launch
import java.time.*
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TimezoneListScreen(navController: NavHostController) {
	val bottomSheetState = rememberModalBottomSheetState(
		initialValue = ModalBottomSheetValue.Hidden,
		animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
	)
	val scope = rememberCoroutineScope()

	BackHandler(enabled = bottomSheetState.isVisible) {
		scope.launch {
			bottomSheetState.hide()
		}
	}

	val onClickAction:(action: ButtonActions)->Unit={
		when(it){
			ButtonActions.BACK_BUTTON -> { navController.popBackStack() }
			ButtonActions.SEARCH_BUTTON -> {  navController.navigate(Route.TimezoneSearch.id) }
			ButtonActions.FILTER_BUTTON -> { scope.launch { bottomSheetState.show() } }
		}
	}

	Scaffold( topBar = { TimezoneListAppBar(onClickAction) } ) {
		val viewModel = LocalTimezoneViewModel.current
		val clockViewModel = LocalClockStateViewModel.current
		val sortType = viewModel.sortType.observeAsState().value
		val timezones =
			viewModel.timezones.value!!.filter { !clockViewModel.containsZone(it.zoneName) }
		val selectedTimezones = LocalClockStateViewModel.current.timezones.value!!

		when (sortType) {
			TimeZoneSort.TIMEZONE -> TimezoneGroupListView(timezones, selectedTimezones)
			else -> CityNameGroupListView(timezones, selectedTimezones)
		}
	}

	TimezoneFilterModal(bottomSheetState = bottomSheetState)
}

val SelectedCitiesListView: LazyListScope.(timezones: List<NativeTimezone>, utcDate: OffsetDateTime, onSelected: (NativeTimezone) -> Unit, isSelected: (String) -> Boolean) -> Unit =
	{ timezones, utcDate, onSelected, isSelected ->
		item {
			Text(
				"SELECTED CITIES",
				modifier = Modifier.padding(bottom = 20.dp, start = 20.dp, top = 4.dp),
				style = MaterialTheme.typography.subtitle2.copy(Color.Gray)
			)
		}

		items(timezones) { timezone ->
			TimezoneItem(
				timezone,
				onSelected = onSelected,
				isSelected = isSelected(timezone.zoneName),
				utcDate = utcDate
			)
		}

		item {
			Text(
				text = "More Cities",
				modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
				style = TextStyle(fontWeight = FontWeight.Medium)
			)
		}
	}

@Composable
fun CityNameGroupListView(
	items: List<NativeTimezone>,
	selectedTimezones: MutableMap<String, NativeTimezone>,
) {
	val clockStateViewModel = LocalClockStateViewModel.current
	val utcDate = LocalUTCTimeViewModel.current.utcDate.value!!
	val groupedItems =
		items.filter { !selectedTimezones.containsKey(it.zoneName) }.groupBy { it.cityName.first() }

	val isSelected: (key: String) -> Boolean = {
		clockStateViewModel.containsZone(it)
	}

	val onSelected: (timezone: NativeTimezone) -> Unit = {
		if (isSelected(it.zoneName)) {
			clockStateViewModel.removeTimezone(it.zoneName)
		} else {
			clockStateViewModel.addTimezone(it)
		}
	}

	LazyColumn(modifier = Modifier.fillMaxSize()) {
		if (selectedTimezones.isNotEmpty()) {
			SelectedCitiesListView(
				selectedTimezones.values.toList(),
				utcDate,
				onSelected,
				isSelected
			)
		}

		groupedItems.forEach { group ->
			item {
				val title = group.key.toString()
				SectionTitle(title = title)
			}

			items(group.value.toList()) { timezone ->
				TimezoneItem(
					timezone,
					isSelected = isSelected(timezone.zoneName),
					onSelected = onSelected,
					utcDate = utcDate
				)
			}
		}
	}
}

@Composable
fun TimezoneGroupListView(
	items: List<NativeTimezone>,
	selectedTimezones: MutableMap<String, NativeTimezone>
) {
	val groupedItems = items.sortedBy { it.gmtOffset }.groupBy { it.gmtOffset }
	val viewmodel = LocalClockStateViewModel.current
	val utcDate = LocalUTCTimeViewModel.current.utcDate.value!!

	val isSelected: (key: String) -> Boolean = {
		viewmodel.containsZone(it)
	}

	val onSelected: (timezone: NativeTimezone) -> Unit = {
		if (isSelected(it.zoneName)) {
			viewmodel.removeTimezone(it.zoneName)
		} else {
			viewmodel.addTimezone(it)
		}
	}

	LazyColumn(modifier = Modifier.fillMaxSize()) {
		if (selectedTimezones.isNotEmpty()) {
			SelectedCitiesListView(
				selectedTimezones.values.toList(),
				utcDate,
				onSelected,
				isSelected
			)
		}

		groupedItems.forEach { group ->
			item {
				val duration = TimeDuration.fromSeconds(group.key.toLong())
				val zone = "GMT ${duration.toZone()}"

				SectionTitle(zone)
			}

			items(group.value.toList()) { timezone ->
				val selected = isSelected(timezone.zoneName)

				TimezoneItem(
					timezone,
					isSelected = selected,
					onSelected = onSelected,
					utcDate = utcDate
				)
			}
		}
	}
}

@Composable
fun SectionTitle(title: String) {
	Text(
		title,
		modifier = Modifier.padding(bottom = 20.dp, start = 20.dp, top = 4.dp),
		style = MaterialTheme.typography.caption.copy(Color.Gray)
	)
}

@Composable
fun TimezoneItem(
	timezone: NativeTimezone,
	isSelected: Boolean = false,
	onSelected: (timezone: NativeTimezone) -> Unit,
	utcDate: OffsetDateTime
) {
	val currentDate = remember(key1 = utcDate) {
		utcDate.plusHours(timezone.duration.hour).plusMinutes(timezone.duration.minutes)
	}

	val checked = remember(key1 = isSelected) { mutableStateOf(isSelected) }

	Row(
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null
			) {
				onSelected(timezone)

				checked.value = !checked.value
			}
			.background(color = if (checked.value) Color.Gray.copy(0.2F) else MaterialTheme.colors.background)
			.padding(horizontal = 20.dp, vertical = 16.dp)
	) {
		Text(text = timezone.cityName)
		Row(verticalAlignment = Alignment.CenterVertically) {
			Text(
				timeFormatter.format(currentDate),
				color = Color.Gray,
				style = TextStyle(fontSize = 11.sp)
			)
			Spacer(modifier = Modifier.width(12.dp))
			CustomCheckbox(checked = checked.value)
		}
	}
}
