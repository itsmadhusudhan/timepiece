package com.shreekaram.timepiece.presentation.clock

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TimezoneSearchScreen(navController: NavHostController) {
	val searchTerm= remember {
		mutableStateOf("")
	}

	val onTextChange:(String)->Unit={
		Log.d("SEARCHTERM",it)
		searchTerm.value=it
	}

	println(searchTerm)

	Scaffold(topBar = { SearchTopBar(navController,searchTerm.value,onTextChange)}) {

		val viewModel = LocalTimezoneViewModel.current
//		val clockViewModel = LocalClockStateViewModel.current
		val timezones = viewModel.timezones.value!!.filter { it.cityName.contains(searchTerm.value,true)  }
		val selectedTimezones= LocalClockStateViewModel.current.timezones.value!!

		when(searchTerm.value.isEmpty()){
			true -> CityNameGroupListView(timezones, selectedTimezones)
			false -> FlatCityList(items = timezones, selectedTimezones = selectedTimezones)
		}
	}
}


@Composable
fun FlatCityList(
	items: List<NativeTimezone>,
	selectedTimezones: MutableMap<String, NativeTimezone>,
){
	val clockStateViewModel=LocalClockStateViewModel.current

	LazyColumn(	modifier = Modifier.fillMaxSize()){
		items(items.size){index->
			val timezone=items[index]
			val isSelected=selectedTimezones.containsKey(timezone.zoneName)

			TimezoneItem(
				timezone = timezone,
				isSelected=isSelected,
				onSelected={
					if(isSelected){
						clockStateViewModel.removeTimezone(it.zoneName)
					}else{
						clockStateViewModel.addTimezone(it)
					}
				})
		}
	}
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchTopBar(navController: NavHostController, searchTerm: String, onTextChange: (term:String)->Unit,){
	val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)
//	val scope = rememberCoroutineScope()

	val focusRequester= remember {
		FocusRequester()
	}

	LaunchedEffect(key1 = true ){
		focusRequester.requestFocus()
	}

	TopAppBar(
		title = {
			TextField(
				value = searchTerm,
				onValueChange = onTextChange,
				placeholder = {Text("Search Cities")},
				colors = TextFieldDefaults.textFieldColors(
					backgroundColor = MaterialTheme.colors.background,
					focusedIndicatorColor = MaterialTheme.colors.background,
					unfocusedIndicatorColor = MaterialTheme.colors.background,
				),
				modifier = Modifier.focusRequester(focusRequester),
				textStyle = TextStyle(fontWeight = FontWeight.Normal)
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
				Text("Cancel")
			}
		}
	)
}