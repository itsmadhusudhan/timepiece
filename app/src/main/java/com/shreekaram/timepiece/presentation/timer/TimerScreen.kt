package com.shreekaram.timepiece.presentation.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TimerScreen(){
	Column(modifier = Modifier
		.fillMaxSize()
		.wrapContentSize(Alignment.Center)) {
		Text(text = "Timer Screen")
	}
}
