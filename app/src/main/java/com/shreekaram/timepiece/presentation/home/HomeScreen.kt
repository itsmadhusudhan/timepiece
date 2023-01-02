package com.shreekaram.timepiece.presentation.home

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.shreekaram.timepiece.presentation.alarm.AlarmScreen
import com.shreekaram.timepiece.presentation.clock.ClockScreen
import com.shreekaram.timepiece.presentation.stopwatch.StopWatchScreen
import com.shreekaram.timepiece.presentation.timer.TimerScreen

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    val homeNavController = rememberAnimatedNavController()

    Scaffold(bottomBar = { BottomNavigationBar(controller = homeNavController) },) {
        AnimatedNavHost(
            startDestination = Route.Worldclock.id,
            navController = homeNavController,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popExitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None }
        ) {
            composable(Route.Worldclock.id,) {
                ClockScreen(navController = navController)
            }

            composable(Route.Alarm.id) {
                AlarmScreen()
            }

            composable(Route.StopWatch.id) {
                StopWatchScreen()
            }

            composable(Route.Timer.id) {
                TimerScreen(navController)
            }
        }
    }
}
