package com.shreekaram.timepiece.presentation.clock

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.domain.clock.*
import com.shreekaram.timepiece.presentation.clock.composables.CustomCheckbox
import com.shreekaram.timepiece.presentation.clock.composables.TimezoneListAppBar
import java.text.SimpleDateFormat
import java.time.*
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TimezoneListScreen(navController: NavHostController) {
	val viewModel: TimezoneViewModel = viewModel()

	val sortType = viewModel.sortType.observeAsState().value
	val timezones = viewModel.timezones.observeAsState().value

	Scaffold(topBar = {
		TimezoneListAppBar(navController)
	}) {
		if (sortType == TimeZoneSort.TIMEZONE)
			GroupedListView(timezones!!)
		else
			FlatListView(items = timezones!!)
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FlatListView(items: List<NativeTimezone>) {
	LazyColumn(
		modifier = Modifier.fillMaxSize()
	) {
		items(
			items = items,
			itemContent = { timezone ->
				TimezoneItem(timezone = timezone)
			},
			key = {
				it.cityName
			}
		)
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupedListView(items: List<NativeTimezone>) {
	val groupedItems = items.sortedBy { it.gmtOffset }.groupBy { it.gmtOffset }

	Log.d("GROUPS",groupedItems.keys.toString())

	LazyColumn(
		modifier = Modifier.fillMaxSize()
	) {
		items(
			items = groupedItems.toList(),
			itemContent = {
				val duration=TimeDuration.fromSeconds(it.first.toLong())
				val zone = "GMT ${duration.toZone()}"

				println(duration)
				Log.d("GROUPS",zone)

				Text(
					zone,
					modifier = Modifier.padding(all = 20.dp),
					style = TextStyle(color = Color.Gray)
				)
				Group(group = it.second)
			},
			key = {
				it.first
			}
		)
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Group(group: List<NativeTimezone>) {
	group.forEach { timezone ->
		TimezoneItem(timezone = timezone)
	}
}

val formatter = SimpleDateFormat("hh:mm a", Locale("en"))

val value = TimeDuration(5, 30, 0)

class UTCDateTimeModel : ViewModel() {
	@RequiresApi(Build.VERSION_CODES.O)
	val date = MutableLiveData<OffsetDateTime>(OffsetDateTime.now(ZoneOffset.UTC))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimezoneItem(timezone: NativeTimezone) {
	val checked = remember {
		mutableStateOf(false)
	}

	val utcDate = UTCDateTimeModel().date.observeAsState()

//	val viewModel = UTCDateTimeModel()

	val viewState = remember {
		utcDate.value!!
			.plusHours(timezone.duration.hour)
			.plusMinutes(timezone.duration.minutes)
			.toInstant()
	}

	val timezoneDate = remember {
		val hours = timezone.duration.hour - value.hour
		val minutes = timezone.duration.minutes - value.minutes

		val date = OffsetDateTime.now(ZoneOffset.UTC)
			.plusHours(timezone.duration.hour)
			.plusMinutes(timezone.duration.minutes).toInstant().toEpochMilli()


		mutableStateOf(Date(date))
	}

	Row(
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				Log.d("Time", viewState.toString())
				checked.value = !checked.value
			}
			.padding(all = 20.dp),
	) {
		Text(text = timezone.cityName)
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(
				formatter.format(timezoneDate.value),
				color = Color.Gray,
				style = TextStyle(fontSize = 12.sp)
			)
			Spacer(modifier = Modifier.width(12.dp))
			CustomCheckbox(checked = checked.value)
		}
	}
}



