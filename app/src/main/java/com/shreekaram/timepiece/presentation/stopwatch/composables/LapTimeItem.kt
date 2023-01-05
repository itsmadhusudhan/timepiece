package com.shreekaram.timepiece.presentation.stopwatch.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreekaram.timepiece.service.stopwatch.formatTime
import com.shreekaram.timepiece.service.stopwatch.pad
import kotlin.time.Duration

@Composable
fun LapTimeItem(item: Duration) {
    val text = item.toComponents { hours, minutes, seconds, _ ->
        return@toComponents formatTime(
            hours = hours.toInt().pad(),
            minutes = minutes.pad(),
            seconds = seconds.pad()
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                "1".padStart(2, '0'),
                fontSize = 12.sp,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text,
                modifier = Modifier.alignByBaseline(),
                style = TextStyle(fontWeight = FontWeight.Light),
                fontSize = 16.sp
            )
        }
        Text(
            text,
            style = TextStyle(fontWeight = FontWeight.Light),
            fontSize = 16.sp
        )
    }
}

fun padZero(value: Int): String {
    return "$value".padStart(2, '0')
}
