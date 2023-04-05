package com.example.mywearos.presentation.ui.landing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListState
import com.example.mywearos.data.SensorData
import com.example.mywearos.presentation.ui.sensordata.RawSensorDataScreen
import com.example.mywearos.presentation.ui.util.ReportFullyDrawn

@Composable
fun LandingScreen(
    scalingLazyListState: ScalingLazyListState,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier.fillMaxSize()){
        ScalingLazyColumn(
            state = scalingLazyListState,
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ){
            item{
                ReportFullyDrawn()
            }
            item{
                RawSensorDataScreen(sensorData = SensorData())
            }
        }
    }
}