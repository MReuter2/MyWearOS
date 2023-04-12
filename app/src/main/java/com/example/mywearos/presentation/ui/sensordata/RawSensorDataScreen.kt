package com.example.mywearos.presentation.ui.sensordata

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.example.mywearos.data.SensorData
import com.example.mywearos.data.stringToTouches


@Composable
fun RawSensorDataScreen(
    sensorData: SensorData,
    modifier: Modifier = Modifier
) {
    val touches = stringToTouches(sensorData.lastTouches)
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
            for (touch in touches) {
                Text(text = touch.toString(), modifier = Modifier.align(CenterHorizontally))
            }
        }
    }
}