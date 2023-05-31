package com.example.mywearos.presentation.ui.sensordata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.example.mywearos.data.SensorData
import com.example.mywearos.data.NoEvent
import com.example.mywearos.data.TrillFlexEvent


@Composable
fun RawSensorDataScreen(
    modifier: Modifier = Modifier,
    sensorDataViewModel: SensorDataViewModel = viewModel()
){
    val latestEvent by sensorDataViewModel.trillFlexEvent.collectAsStateWithLifecycle(NoEvent())
    val latestSensorData by sensorDataViewModel.sensorData.collectAsStateWithLifecycle(SensorData(emptyList()))
    RawSensorDataScreen(modifier, latestEvent, latestSensorData)
}


@Composable
private fun RawSensorDataScreen(
    modifier: Modifier = Modifier,
    latestEvent: TrillFlexEvent,
    latestSensorData: SensorData
) {
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
            Text(text = "Event: $latestEvent", modifier = Modifier.align(CenterHorizontally))
            for (data in latestSensorData.locationsWithSize) {
                Text(text = "Location: ${data.first}, Size: ${data.second}", modifier = Modifier.align(CenterHorizontally))
            }
        }
    }
}