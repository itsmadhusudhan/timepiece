package com.shreekaram.timepiece.presentation.timer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

enum class ButtonActions {
    SETTINGS_BUTTON,
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TimerScreen(navController: NavHostController) {
    val onClickAction: (action: ButtonActions) -> Unit = {
        when (it) {
            ButtonActions.SETTINGS_BUTTON -> { }
        }
    }

    Scaffold(
        topBar = {
			
            TimerAppBar(onClickAction)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text(text = "Timer Screen")
        }
    }
}

@Composable
fun TimerAppBar(onClickAction: (action: ButtonActions) -> Unit) {

    val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

    TopAppBar(
        title = { Text("Timer", fontSize = 16.sp) },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 0.dp,
        modifier = Modifier.drawBehind {
            drawLine(
                borderColor,
                Offset(0F, size.height),
                Offset(size.width, size.height),
                1F
            )
        },
        actions = {
            IconButton(onClick = { onClickAction(ButtonActions.SETTINGS_BUTTON) }) {
                Icon(Icons.Outlined.Settings, "Settings")
            }
        }
    )
}
