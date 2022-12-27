package com.shreekaram.timepiece.presentation.clock

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.LocalTimezoneViewModel
import com.shreekaram.timepiece.domain.clock.*
import com.shreekaram.timepiece.presentation.clock.composables.CustomCheckbox
import com.shreekaram.timepiece.presentation.clock.composables.TimeZoneTypeSheet
import com.shreekaram.timepiece.presentation.clock.composables.TimezoneListAppBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.*
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TimezoneListScreen(navController: NavHostController) {
	val viewModel = LocalTimezoneViewModel.current
	val sortType = viewModel.sortType.observeAsState().value
	val timezones = viewModel.timezones.observeAsState().value!!

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

	Scaffold(
		topBar = {
			TimezoneListAppBar(navController, bottomSheetState)
		}
	) {
		if (sortType == TimeZoneSort.TIMEZONE) {
			TimezoneGroupListView(timezones)
		} else {
			CityNameGroupListView(timezones)
		}
	}

	TimeZoneTypeSheet(bottomSheetState = bottomSheetState)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CityNameGroupListView(items: List<NativeTimezone>) {
	val groupedItems = items.groupBy { it.cityName.first() }

	LazyColumn(
		modifier = Modifier.fillMaxSize()
	) {
		groupedItems.forEach { group ->
			items(group.value.size + 1) { index ->
				if (index == 0) {
					val title = group.key.toString()

					SectionTitle(title = title)
				} else {
					TimezoneItem(timezone = group.value.get(index - 1))
				}
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimezoneGroupListView(items: List<NativeTimezone>) {
	val groupedItems = items.sortedBy { it.gmtOffset }.groupBy { it.gmtOffset }

	LazyColumn(
		modifier = Modifier.fillMaxSize()
	) {
		groupedItems.forEach { group ->
			items(group.value.size + 1) { index ->
				if (index == 0) {
					val duration = TimeDuration.fromSeconds(group.key.toLong())
					val zone = "GMT ${duration.toZone()}"

					SectionTitle(zone)
				} else {
					TimezoneItem(timezone = group.value.get(index - 1))
				}
			}
		}
	}
}

@Composable
fun SectionTitle(title: String) {
	Text(
		title,
		modifier = Modifier.padding(all = 20.dp),
		style = MaterialTheme.typography.caption.copy(Color.Gray)
	)
}

val formatter = SimpleDateFormat("hh:mm a", Locale("en"))

//val value = TimeDuration(5, 30, 0)

// FIXME: refactor this
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

//	val viewState = remember {
//		utcDate.value!!.plusHours(timezone.duration.hour).plusMinutes(timezone.duration.minutes)
//			.toInstant()
//	}

	val timezoneDate = remember {
//		val hours = timezone.duration.hour - value.hour
//		val minutes = timezone.duration.minutes - value.minutes

		val date = utcDate.value!!.plusHours(timezone.duration.hour)
			.plusMinutes(timezone.duration.minutes).toInstant().toEpochMilli()

		mutableStateOf(Date(date))
	}

	Row(
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null
//				indication = rememberRipple(
//					color = RippleTheme.defaultRippleColor(Color.Gray, true)
//				),
			) {
				checked.value = !checked.value
			}
			.background(color = if (checked.value) Color.Gray.copy(0.2F) else MaterialTheme.colors.background)
			.padding(horizontal = 20.dp, vertical = 16.dp)
	) {
		Text(text = timezone.cityName)
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(
				formatter.format(timezoneDate.value),
				color = Color.Gray,
				style = TextStyle(fontSize = 11.sp)
			)
			Spacer(modifier = Modifier.width(12.dp))
			CustomCheckbox(checked = checked.value)
		}
	}
}


/**
 *
 * 			.drawWithContent {
drawContent()
val firstVisibleElementIndex =
state.layoutInfo.visibleItemsInfo.firstOrNull()?.index

if (firstVisibleElementIndex != null) {

val scrollableItems =
state.layoutInfo.totalItemsCount - state.layoutInfo.visibleItemsInfo.size
val scrollBarHeight = this.size.height / scrollableItems
var offsetY =
((this.size.height - scrollBarHeight) * firstVisibleElementIndex) / scrollableItems

drawRect(
color = bgColor,
topLeft = Offset(x = this.size.width-10, y = 0F),
size = Size(12F, 200F),
alpha = 1f
)

drawRect(
color = Color.LightGray,
topLeft = Offset(x = this.size.width, y = offsetY),
size = Size(12F, scrollBarHeight),
alpha = 1f
)
}
},

 */