package com.shreekaram.timepiece.presentation.stopwatch.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.shreekaram.timepiece.presentation.stopwatch.ButtonActions

@Composable
fun ActionFabButton(
    icon: ImageVector,
    iconLabel: String,
    action: ButtonActions,
    onClickAction: (action: ButtonActions) -> Unit
) {
    FloatingActionButton(
        modifier = Modifier.padding(bottom = 60.dp),
        onClick = {
            onClickAction(action)
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = FloatingActionButtonDefaults.elevation(0.dp),
        interactionSource = MutableInteractionSource(),
        content = { Icon(icon, iconLabel) },
        contentColor = MaterialTheme.colors.onBackground
    )
}
