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
    val sensorData = remember { sensorDataViewModel.allSensorData }
    val event = remember { sensorDataViewModel.allEvents }
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
            if(event.isNotEmpty()){
                Text(text = "Event: ${event.last()}", modifier = Modifier.align(CenterHorizontally))
            }
            if(sensorData.isNotEmpty()){
                for (locationWithSize in sensorData.last().locationsWithSize) {
                    Text(text = "Location: ${locationWithSize.first}, Size: ${locationWithSize.second}", modifier = Modifier.align(CenterHorizontally))
                }
            }
        }
    }
}