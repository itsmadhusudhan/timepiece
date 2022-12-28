package com.shreekaram.timepiece.presentation.home

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shreekaram.timepiece.presentation.clock.ClockScreen
import com.shreekaram.timepiece.presentation.alarm.AlarmScreen
import com.shreekaram.timepiece.presentation.stopwatch.StopWatchScreen
import com.shreekaram.timepiece.presentation.timer.TimerScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController:NavHostController){
	val homeNavController = rememberNavController()

	Scaffold(
		bottomBar = { BottomNavigationBar(controller = homeNavController) },
	){
		NavHost(
			startDestination= Route.Worldclock.id,
			navController = homeNavController
		){
			composable(Route.Worldclock.id){
				ClockScreen(navController=navController)
			}

			composable(Route.Alarm.id){
				AlarmScreen()
			}

			composable(Route.StopWatch.id){
				StopWatchScreen()
			}

			composable(Route.Timer.id){
				TimerScreen()
			}
		}
	}
}
