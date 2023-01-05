package com.shreekaram.timepiece.presentation.clock

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shreekaram.timepiece.LocalClockStateViewModel
import com.shreekaram.timepiece.LocalUTCTimeViewModel
import com.shreekaram.timepiece.domain.clock.NativeTimezone

@Composable
fun ClockListView() {
    val timezones = LocalClockStateViewModel.current.timezones.observeAsState().value!!.toList()
    val clockStateModel = hiltViewModel<ClockStateViewModel>()

    Log.d("STATE", "clock state")
    println(clockStateModel.homeTimezone.value)

    val currentTimezone = clockStateModel.currentTimezone.observeAsState().value!!
    val homeTimezone = clockStateModel.homeTimezone.observeAsState().value!!

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .wrapContentHeight()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 120.dp),
        userScrollEnabled = timezones.isNotEmpty()
    ) {
        item {
            ClockCanvas(currentTimezone)
        }
        if (homeTimezone.zoneName != currentTimezone.zoneName) {
            item {
                LocalTimeItem(homeTimezone.copy(cityName = "Home"))
            }
        }
        items(timezones.size) { index ->
            val timezone = timezones[index]
// 			loop and pass selected native time zones
            LocalTimeItem(timezone.second)
        }
    }
}

@Composable
fun LocalTimeItem(timezone: NativeTimezone) {
    val currentDate = LocalUTCTimeViewModel.current.utcDate.observeAsState().value!!
        .plusHours(timezone.duration.hour)
        .plusMinutes(timezone.duration.minutes)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = timezone.cityName,
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.W300)
        )

        val time = timeFormatter.format(currentDate).split(" ")

        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = time.first(),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.W300
                    ),
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = time.last().lowercase(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W300
                    ),
                    modifier = Modifier.alignByBaseline()
                )
            }
            Text(
                text = dateFormatter.format(currentDate),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W300,
                    color = Color.Gray
                )
            )
        }
    }
}
