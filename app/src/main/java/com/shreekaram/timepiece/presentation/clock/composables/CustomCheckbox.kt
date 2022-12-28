package com.shreekaram.timepiece.presentation.clock.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomCheckbox(checked: Boolean) {
	val unCheckedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.4F)
	val tint = if (checked) MaterialTheme.colors.primary else unCheckedColor

	Icon(
		if (checked) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
		"",
		tint = tint,
		modifier = Modifier
			.size(20.dp)
	)
}