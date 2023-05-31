package com.example.mywearos.presentation.ui.sensordata

import androidx.lifecycle.ViewModel
import com.example.mywearos.data.SensorData
import com.example.mywearos.data.TrillFlexEvent
import com.example.mywearos.model.TrillFlexSensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SensorDataViewModel: ViewModel() {
    private val _trillFlex = TrillFlexSensor()
    val trillFlexEvent: Flow<TrillFlexEvent> = _trillFlex.events ?: flowOf()
    val sensorData: Flow<SensorData> = _trillFlex.sensorData ?: flowOf()
}