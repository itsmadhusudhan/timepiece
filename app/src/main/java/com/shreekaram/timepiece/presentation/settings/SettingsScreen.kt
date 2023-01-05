package com.shreekaram.timepiece.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shreekaram.timepiece.LocalClockStateViewModel
import com.shreekaram.timepiece.domain.clock.toGmtZone
import com.shreekaram.timepiece.presentation.home.Route

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavHostController) {
    val borderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5F)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontSize = 16.sp) },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            borderColor,
                            Offset(0F, size.height),
                            Offset(size.width, size.height),
                            1F
                        )
                    }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(vertical = 20.dp)) {
            item {
                val homeTimezone =
                    LocalClockStateViewModel.current.homeTimezone.observeAsState().value!!

                val subtitle = "(GMT ${homeTimezone.duration.toGmtZone()}) ${homeTimezone.cityName}"

                Text(
                    "TIME ZONE PREFERENCES",
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Color.Gray,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier.padding(start = 28.dp)
                )

                OptionItem(
                    title = "Home Time Zone",
                    subtitle = subtitle,
                    { navController.navigate(Route.HomeTimezoneList.id) }
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp))
            }
        }
    }
}

@Composable
fun OptionItem(title: String, subtitle: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column() {
                Text(title, style = MaterialTheme.typography.body1)
                Text(subtitle, style = MaterialTheme.typography.caption.copy(color = Color.Gray))
            }
        }
    }
}
