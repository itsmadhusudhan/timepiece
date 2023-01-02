package com.shreekaram.timepiece.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.LocalClockStateViewModel
import com.shreekaram.timepiece.LocalTimezoneViewModel
import com.shreekaram.timepiece.domain.clock.NativeTimezone
import com.shreekaram.timepiece.presentation.home.Route

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeTimezoneSearchScreen(navController: NavHostController) {
    val searchTerm = remember {
        mutableStateOf("")
    }

    val onTextChange: (String) -> Unit = {
        searchTerm.value = it
    }

    Scaffold(topBar = { SearchTopBar(navController, searchTerm.value, onTextChange) }) {
        val clockStateViewModel = LocalClockStateViewModel.current
        val viewModel = LocalTimezoneViewModel.current
        val timezones = viewModel.timezones.value!!.filter { it.cityName.startsWith(searchTerm.value, true) }
        val homeTimezone = clockStateViewModel.homeTimezone.value!!

        val onSelected: (timezone: NativeTimezone) -> Unit = { timezone ->
            clockStateViewModel.updateHomeTimezone(timezone)

            navController.popBackStack(route = Route.Settings.id, inclusive = false)
        }

        when (searchTerm.value.isEmpty()) {
            true -> CityNameGroupListView(timezones, onSelected = onSelected, isSelected = { homeTimezone.zoneName == it })
            false -> FlatCityList(timezones, onSelected = onSelected, isSelected = { homeTimezone.zoneName == it })
        }
    }
}

@Composable
fun FlatCityList(
    timezones: List<NativeTimezone>,
    isSelected: (key: String) -> Boolean,
    onSelected: (timezone: NativeTimezone) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(timezones) { timezone ->
            TimezoneItemSelect(
                timezone = timezone,
                isSelected = isSelected(timezone.zoneName),
                onSelected = onSelected,
            )
        }
    }
}

@Composable
fun SearchTopBar(
    navController: NavHostController,
    searchTerm: String,
    onTextChange: (term: String) -> Unit,
) {
    val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
    }

    TopAppBar(
        title = {
            TextField(
                value = searchTerm,
                onValueChange = onTextChange,
                placeholder = { Text("Search Cities") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    focusedIndicatorColor = MaterialTheme.colors.background,
                    unfocusedIndicatorColor = MaterialTheme.colors.background,
                ),
                modifier = Modifier.focusRequester(focusRequester),
                textStyle = TextStyle(fontWeight = FontWeight.Normal),
                trailingIcon = {
                    if (searchTerm.isNotEmpty()) {
                        IconButton(onClick = { onTextChange("") }) {
                            Icon(
                                Icons.Filled.Clear,
                                "Clear search",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
            )
        },
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
            TextButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text("Cancel", color = MaterialTheme.colors.onBackground)
            }
        }
    )
}
