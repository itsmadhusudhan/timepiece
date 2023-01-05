package com.shreekaram.timepiece.presentation.stopwatch.composables

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.shreekaram.timepiece.presentation.stopwatch.ButtonActions

@Composable
fun StopWatchAppBar(onClickAction: (action: ButtonActions) -> Unit) {
    return TopAppBar(
        title = {},
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.onBackground,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { onClickAction(ButtonActions.SETTINGS_BUTTON) }) {
                Icon(Icons.Outlined.Settings, "settings action")
            }
        }
    )
}
