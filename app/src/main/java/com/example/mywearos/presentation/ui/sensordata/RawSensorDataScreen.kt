package com.example.mywearos.presentation.ui.sensordata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        for (touch in touches) {
            Text(text = touch.toString())
        }
    }
}