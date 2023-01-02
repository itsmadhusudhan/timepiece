package com.shreekaram.timepiece.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.LocalClockStateViewModel
import com.shreekaram.timepiece.LocalTimezoneViewModel
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.domain.clock.toGmtZone
import com.shreekaram.timepiece.presentation.clock.SectionTitle
import com.shreekaram.timepiece.presentation.home.Route

enum class ButtonActions {
    BACK_BUTTON,
    SEARCH_BUTTON
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeTimezoneListScreen(navController: NavHostController) {

    val onClickAction: (action: ButtonActions) -> Unit = {
        when (it) {
            ButtonActions.BACK_BUTTON -> { navController.popBackStack() }
            ButtonActions.SEARCH_BUTTON -> { navController.navigate(Route.HomeTimezoneSearch.id) }
        }
    }

    Scaffold(topBar = { AppBar(onClickAction) }) {
        val configuration = LocalConfiguration.current
        val clockStateViewModel = LocalClockStateViewModel.current
        val timezones = LocalTimezoneViewModel.current.timezones.value!!
        val state = rememberLazyListState()

        val homeTimezone = clockStateViewModel.homeTimezone.value!!
        val index = timezones.indexOfFirst { it.zoneName == homeTimezone.zoneName }
        val density = LocalDensity.current

        LaunchedEffect(key1 = true) {
            state.layoutInfo.totalItemsCount
            val offset = with(density) { (configuration.screenHeightDp - 140).dp.toPx() }
            state.scrollToItem(index, offset.toInt())
        }

        val onSelected: (timezone: NativeTimezone) -> Unit = { timezone ->
            clockStateViewModel.updateHomeTimezone(timezone)

            navController.popBackStack()
        }

        CityNameGroupListView(
            timezones = timezones,
            state = state,
            onSelected = onSelected,
            isSelected = { homeTimezone.zoneName == it }
        )
    }
}

@Composable
fun CityNameGroupListView(
    timezones: List<NativeTimezone>,
    isSelected: (key: String) -> Boolean,
    onSelected: (timezone: NativeTimezone) -> Unit,
    state: LazyListState = LazyListState(),
) {
    val groupedItems = timezones.groupBy { it.cityName.first() }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = state) {
        groupedItems.forEach { group ->
            item {
                val title = group.key.toString()
                SectionTitle(title = title)
            }

            items(group.value.toList()) { timezone ->
                TimezoneItemSelect(
                    timezone,
                    isSelected = isSelected(timezone.zoneName),
                    onSelected = onSelected,
                )
            }
        }
    }
}

@Composable
fun AppBar(onClickAction: (action: ButtonActions) -> Unit) {
    val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

    TopAppBar(
        title = { Text("Home time zone", fontSize = 16.sp) },
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
                Icon(Icons.Outlined.Search, "search home timezones")
            }
        }
    )
}

@Composable
fun TimezoneItemSelect(
    timezone: NativeTimezone,
    isSelected: Boolean = false,
    onSelected: (timezone: NativeTimezone) -> Unit,
) {
    val label = "GMT ${timezone.duration.toGmtZone()}"

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelected(timezone)
            }
            .background(color = if (isSelected) Color.Gray.copy(0.1F) else MaterialTheme.colors.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(text = timezone.cityName)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioSelect(value = timezone, label = label, selected = isSelected, onClick = onSelected)
        }
    }
}

@Composable
fun <T> RadioSelect(value: T, label: String, selected: Boolean, onClick: (option: T) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,) {
        Text(
            label,
            color = Color.Gray,
            style = TextStyle(fontSize = 12.sp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        RadioButton(
            selected = selected,
            modifier = Modifier
                .padding(all = Dp(value = 0F))
                .wrapContentWidth()
                .size(20.dp),
            onClick = {
                onClick(value)
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = Color.Gray.copy(alpha = 0.5F)
            )
        )
    }
}
