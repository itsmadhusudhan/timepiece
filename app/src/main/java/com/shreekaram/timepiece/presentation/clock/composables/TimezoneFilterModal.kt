package com.shreekaram.timepiece.presentation.clock.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shreekaram.timepiece.LocalClockStateViewModel
import com.shreekaram.timepiece.presentation.clock.TimeZoneSort
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimezoneFilterModal(bottomSheetState: ModalBottomSheetState) {
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        sheetContent = {
            BottomSheetContent(bottomSheetState)
        },
        scrimColor = Color.Black.copy(alpha = 0.25f),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(bottomSheetState: ModalBottomSheetState) {
    val viewModel = LocalClockStateViewModel.current

    val sortType = viewModel.sortType.observeAsState().value
    val scope = rememberCoroutineScope()

    val onClick: (option: TimeZoneSort) -> Unit = {

        viewModel.updateSortType(it)

        scope.launch {
            bottomSheetState.hide()
        }
    }

    Surface(
        modifier = Modifier
            .height(200.dp)
            .padding(top = 24.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Sort Cities By",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                )
            }
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )
            LazyColumn {
                items(TimeZoneSort.values()) { option ->
                    RadioLabel(
                        option = option,
                        label = option.value,
                        selected = sortType == option,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
fun <T> RadioLabel(option: T, label: String, selected: Boolean, onClick: (option: T) -> Unit) {
    Row(
        modifier = Modifier
            .selectable(
                selected = selected,
                onClick = {
                    onClick(option)
                },
                role = Role.RadioButton
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 20.dp)
            .size(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Text(label)
        RadioButton(
            selected = selected,
            modifier = Modifier.padding(all = Dp(value = 0F)),
            onClick = {
                onClick(option)
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary
            )
        )
    }
}
