package com.shreekaram.timepiece.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.shreekaram.timepiece.presentation.clock.TimezoneListScreen
import com.shreekaram.timepiece.presentation.clock.TimezoneSearchScreen
import com.shreekaram.timepiece.presentation.settings.HomeTimezoneListScreen
import com.shreekaram.timepiece.presentation.settings.HomeTimezoneSearchScreen
import com.shreekaram.timepiece.presentation.settings.SettingsScreen

sealed class Route(var title: String, var id: String) {
    object Root : Route(title = "Root", id = "root")
    object Home : Route(title = "Home", id = "home")
    object Worldclock : Route(title = "World clock", id = "worldclock")
    object Alarm : Route(title = "Alarm", id = "alarm")
    object Timer : Route(title = "Timer", id = "timer")
    object StopWatch : Route(title = "Stopwatch", id = "stopwatch")
    object Settings : Route(title = "Settings", id = "settings")
    object TimezoneList : Route(title = "TimezoneList", id = "timezonelist")
    object TimezoneSearch : Route(title = "TimezoneSearch", id = "timezonesearch")
    object HomeTimezoneList : Route(title = "HomeTimezoneList", id = "hometimezonelist")
    object HomeTimezoneSearch : Route(title = "HomeTimezoneSearch", id = "hometimezonesearch")
}

sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector,
    var route: String,
    var selectedIcon: ImageVector
) {
    object WorldClock : BottomNavItem(
        title = Route.Worldclock.title,
        icon = Icons.Filled.Schedule,
        route = Route.Worldclock.id,
        selectedIcon = Icons.Filled.WatchLater
    )

    object Alarm : BottomNavItem(
        Route.Alarm.title,
        Icons.Filled.AccessAlarms,
        Route.Alarm.id,
        Icons.Filled.AccessAlarms
    )

    object Timer : BottomNavItem(
        Route.Timer.title,
        Icons.Filled.HourglassBottom,
        Route.Timer.id,
        Icons.Filled.HourglassBottom
    )

    object StopWatch : BottomNavItem(
        Route.StopWatch.title,
        Icons.Filled.Timelapse,
        Route.StopWatch.id,
        Icons.Filled.Timelapse
    )
}

@Composable
fun BottomNavigationBar(controller: NavHostController) {
    val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 0.dp,
        modifier = Modifier
            .drawBehind {
                drawLine(
                    borderColor,
                    Offset(0f, 0F),
                    Offset(size.width, 0F),
                    1F
                )
            }

    ) {
        val items = listOf(
            BottomNavItem.Alarm,
            BottomNavItem.WorldClock,
            BottomNavItem.Timer,
            BottomNavItem.StopWatch,
        )
        val stackEntry by controller.currentBackStackEntryAsState()
        val currentRoute = stackEntry?.destination?.route

        items.forEach {
            val selected = currentRoute == it.route

            BottomNavigationItem(
                unselectedContentColor = MaterialTheme.colors.onBackground.copy(alpha = 0.5F),
                selectedContentColor = MaterialTheme.colors.onBackground,
                icon = {
                    Icon(
                        imageVector = if (selected) it.selectedIcon else it.icon,
                        contentDescription = it.title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = { Text(text = it.title, fontSize = 10.sp) },
                selected = selected,
                onClick = {
                    controller.navigate(it.route) {

                        controller.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// @OptIn(ExperimentalAnimationApi::class)
// @Composable
// fun EnterAnimation(content: @Composable () AnimatedVisibilityScope.() -> Unit) {
// 	val visible = remember {
// 		MutableTransitionState(false).apply { targetState= true }
// 	}
//
// 	AnimatedVisibility(
// 		visibleState = visible,
// 		enter = slideInHorizontally (
// 			initialOffsetX = { 300 }
// 		) + fadeIn(initialAlpha = 0.3f),
// 		exit = slideOutHorizontally(targetOffsetX = {0}) + fadeOut(),
// 		content = content,
// 	)
// }

@OptIn(ExperimentalAnimationApi::class)
val onEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?) = {
    slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(400))
}

@OptIn(ExperimentalAnimationApi::class)
val onPopExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?) = {
    slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(400))
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RootNavigationGraph(navController: NavHostController) {

    AnimatedNavHost(startDestination = Route.Home.id, navController = navController) {
        composable(
            Route.Home.id,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            HomeScreen(navController = navController)
        }

        navigation(
            startDestination = Route.Settings.id,
            route = Route.Root.id,
        ) {
            composable(
                Route.Settings.id,
                enterTransition = {
                    when (initialState.destination.route) {
                        Route.HomeTimezoneList.id, Route.HomeTimezoneSearch.id -> null
                        else -> onEnterTransition()
                    }
                },
                popExitTransition = onPopExitTransition
            ) {
                SettingsScreen(navController = navController)
            }

            composable(
                Route.HomeTimezoneList.id,
                enterTransition = {
                    when (initialState.destination.route) {
                        Route.HomeTimezoneSearch.id -> null
                        else -> onEnterTransition()
                    }
                },
                popExitTransition = onPopExitTransition
            ) {
                HomeTimezoneListScreen(navController = navController)
            }

            composable(
                Route.HomeTimezoneSearch.id,
                enterTransition = onEnterTransition,
                popExitTransition = onPopExitTransition
            ) {
                HomeTimezoneSearchScreen(navController = navController)
            }
        }

        composable(
            Route.TimezoneList.id,
            enterTransition = {
                when (initialState.destination.route) {
                    Route.TimezoneSearch.id -> null
                    else -> onEnterTransition()
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Route.Home.id -> null
                    else -> onPopExitTransition()
                }
            }
        ) {
            TimezoneListScreen(navController = navController)
        }

        composable(
            Route.TimezoneSearch.id,
            enterTransition = onEnterTransition,
            popExitTransition = onPopExitTransition
        ) {
            TimezoneSearchScreen(navController = navController)
        }
    }
}
