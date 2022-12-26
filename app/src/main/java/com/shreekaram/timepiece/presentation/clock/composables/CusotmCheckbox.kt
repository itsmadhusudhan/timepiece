package com.shreekaram.timepiece.presentation.clock.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable

@Composable
fun CustomCheckbox(checked: Boolean) {
	Box() {
		Icon(
			if (checked) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
			"",
			tint = if (checked) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
		)
	}
}