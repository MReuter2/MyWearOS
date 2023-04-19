package com.example.mywearos.presentation.ui.sensordata

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text


@Composable
fun RawSensorDataScreen(
    sensorDataViewModel: SensorDataViewModel,
    modifier: Modifier = Modifier
) {
    val touches = remember { sensorDataViewModel.latestTouches }
    val isScroll = remember { sensorDataViewModel.isScroll }
    val scroll = remember { sensorDataViewModel.scroll }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ) {
            if(isScroll.value)
                Text(text = scroll.value.toString(), modifier = Modifier.align(CenterHorizontally))
            for (touch in touches) {
                Text(text = touch.toString(), modifier = Modifier.align(CenterHorizontally))
            }
        }
    }
}