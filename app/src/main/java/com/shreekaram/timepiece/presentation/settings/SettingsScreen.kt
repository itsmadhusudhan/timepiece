package com.shreekaram.timepiece.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavHostController){
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Settings") },
				backgroundColor = MaterialTheme.colors.background,
				contentColor = MaterialTheme.colors.onBackground,
				elevation = 0.dp,
				navigationIcon = {
					IconButton(onClick = {
						navController.popBackStack()
					}) {
						Icon(Icons.Filled.ArrowBack,"Back")
					}
				}
			)

		}
	) {
		Column(modifier = Modifier
			.fillMaxSize()
			.wrapContentSize(Alignment.Center)) {
			Text(text = "Settings Screen")
		}
	}
}