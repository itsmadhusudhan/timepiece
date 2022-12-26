package com.shreekaram.timepiece.presentation

import android.content.Context
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.compose.animation.*
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import com.shreekaram.timepiece.presentation.clock.ClockCanvas
import com.shreekaram.timepiece.presentation.home.Route

val formatter= SimpleDateFormat("hh:mm a, E, M/yy", Locale("en"))

//val objectArrayString: String = context.resources.openRawResource(R.raw.my_object).bufferedReader().use { it.readText() }
//val objectArray = Gson().fromJson(objectArrayString, MyObject::class.java)

inline fun <reified T> Context.jsonToClass(@RawRes resourceId: Int): T =
	Gson().fromJson(resources.openRawResource(resourceId).bufferedReader().use { it.readText() }, T::class.java)

@Composable
fun ClockScreen(navController: NavHostController){
	val scroll: ScrollState = rememberScrollState(0)
	val ctx = LocalContext.current

	Scaffold (
		topBar = { TopBar(title = "", navController= navController) },
		floatingActionButton = {
			FloatingActionButton(
				modifier = Modifier.padding(bottom = 60.dp),
				onClick = {
					navController.navigate(Route.TimezoneList.id)
				},
				backgroundColor = MaterialTheme.colors.primary,
			) {
				Icon(Icons.Filled.Language, "Timezones")
			}
		},
		floatingActionButtonPosition = FabPosition.Center,
		isFloatingActionButtonDocked = true,
		) {

		Column(modifier = Modifier
			.wrapContentSize()
			.fillMaxSize()
			.padding(horizontal = 16.dp, vertical = it.calculateBottomPadding())
		){

			Column(
				modifier = Modifier
					.wrapContentSize()
					.verticalScroll(scroll)
			) {
				Text(
					text = "World Clock",
					style = MaterialTheme.typography.h3
				)
				Spacer(Modifier.size(8.dp))
				Today()
			}

			ClockCanvas()

		}


	}
}


//@Composable
//private fun Header(scroll: ScrollState, headerHeightPx: Float) {
//
//	Box(modifier = Modifier
//		.fillMaxWidth()
//		.height(headerHeight)
//		.graphicsLayer {
//			translationY = -scroll.value.toFloat() / 2f // Parallax effect
//		}
//	)
//}


@Composable
fun Today(){
	var currentDate by remember{
		mutableStateOf(Date())
	}

	LaunchedEffect(key1 = currentDate ){
		delay(1000L)
		currentDate= Date()
	}

	Text(
		text = formatter.format(currentDate),
		color= Color.Gray,
		style =  MaterialTheme.typography.caption
	)
}

@Composable
fun TopBar(title:String,navController: NavHostController) {
	TopAppBar(
		title = { Text(text = title, fontSize = 18.sp) },
		backgroundColor = MaterialTheme.colors.background,
		contentColor = MaterialTheme.colors.onBackground,
		elevation = 0.dp,
		actions= {
			IconButton(
				onClick = {
					navController.navigate(Route.Settings.id)
				}
			) {
				Icon(imageVector = Icons.Filled.Settings,"Settings" )
			}
		}
	)
}
