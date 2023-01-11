package com.shreekaram.timepiece.presentation.stopwatch

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.presentation.home.Route
import com.shreekaram.timepiece.presentation.stopwatch.composables.ActionFabButton
import com.shreekaram.timepiece.presentation.stopwatch.composables.LapTimeItem
import com.shreekaram.timepiece.presentation.stopwatch.composables.StopWatchAppBar
import com.shreekaram.timepiece.service.stopwatch.ServiceHelper
import com.shreekaram.timepiece.service.stopwatch.StopWatchCommand
import com.shreekaram.timepiece.service.stopwatch.StopWatchState

enum class ButtonActions {
    SETTINGS_BUTTON,
    TOGGLE_PLAY,
    RESET_BUTTON,
    SHARE_BUTTON,
    LAPSE_BUTTON,
}

data class LapseTime(
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val millisecond: Int = 0,
    val angle: Float = 0F
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StopWatchScreen(navController: NavHostController) {
    val state = rememberLazyListState()
    val timerViewModel = hiltViewModel<StopWatchViewModel>()
    val lapseList = timerViewModel.lapseList.observeAsState()
    val context = LocalContext.current

    val tempState = timerViewModel.state.observeAsState()

    val isPlaying = tempState.value == StopWatchState.STARTED

    val onClickAction: (action: ButtonActions) -> Unit = {
        when (it) {
            ButtonActions.SETTINGS_BUTTON -> navController.navigate(Route.Settings.id)
            ButtonActions.TOGGLE_PLAY -> {
                val command = when (isPlaying) {
                    true -> StopWatchCommand.PAUSE_SERVICE
                    false -> StopWatchCommand.START_SERVICE
                }

                ServiceHelper.triggerForegroundService(context, command)
            }
            ButtonActions.RESET_BUTTON -> {
                ServiceHelper.triggerForegroundService(context, StopWatchCommand.CANCEL_SERVICE)
            }
            ButtonActions.SHARE_BUTTON -> {}
            ButtonActions.LAPSE_BUTTON -> {
                timerViewModel.lapse()
            }
        }
    }

    Scaffold(
        topBar = { StopWatchAppBar(onClickAction) },
        floatingActionButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ActionFabButton(
                    icon = Icons.Filled.Refresh,
                    iconLabel = "reset",
                    action = ButtonActions.RESET_BUTTON,
                    onClickAction = onClickAction
                )
                FloatingActionButton(isPlaying, onClickAction)
                if (!isPlaying) {
                    ActionFabButton(
                        icon = Icons.Filled.Share,
                        iconLabel = "share",
                        action = ButtonActions.SHARE_BUTTON,
                        onClickAction = onClickAction
                    )
                }

                if (isPlaying) {
                    ActionFabButton(
                        icon = Icons.Outlined.Flag,
                        iconLabel = "lap",
                        action = ButtonActions.LAPSE_BUTTON,
                        onClickAction = onClickAction
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Stopwatch", style = MaterialTheme.typography.h4)
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight(0.8F)
            ) {
                StopWatchCanvas()

                AnimatedVisibility(visible = lapseList.value!!.size > 0) {
                    Column {
                        LapHeader()
                        LazyColumn(
                            reverseLayout = true,
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            state = state
                        ) {
                            items(lapseList.value!!.toList()) { item ->
                                LapTimeItem(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingActionButton(isPlaying: Boolean, onClickAction: (action: ButtonActions) -> Unit) {
    FloatingActionButton(
        modifier = Modifier.padding(bottom = 60.dp),
        onClick = {
            onClickAction(ButtonActions.TOGGLE_PLAY)
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, "play")
    }
}

@Composable
fun ShareButton() {
    FloatingActionButton(
        modifier = Modifier.padding(bottom = 60.dp),
        onClick = {
        },
        backgroundColor = Color.White,
        elevation = FloatingActionButtonDefaults.elevation(0.dp),
        interactionSource = MutableInteractionSource()
    ) {
        Icon(Icons.Filled.Share, "play")
    }
}

@Composable
fun LapHeader() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(bottom = 12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Spacer(modifier = Modifier.width(16.dp))
            Text("Lap Time", fontSize = 12.sp)
        }
        Text("Split Time", fontSize = 12.sp)
    }
}
