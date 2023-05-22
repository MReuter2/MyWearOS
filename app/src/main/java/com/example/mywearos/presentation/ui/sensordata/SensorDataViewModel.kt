package com.example.mywearos.presentation.ui.sensordata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywearos.data.sensor.RawSensorData
import com.example.mywearos.data.sensor.SensorDataReceiver
import com.example.mywearos.data.sensor.TrillFlexEvent
import com.example.mywearos.data.sensor.TrillFlexSensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SensorDataViewModel: ViewModel() {
    private val _sensorDataReceiver = SensorDataReceiver()
    private val _trillFlex = TrillFlexSensor()
    private val _trillFlexEvent: MutableStateFlow<TrillFlexEvent> = _trillFlex.events
    val trillFlexEvent: Flow<TrillFlexEvent> = _trillFlexEvent
    private val _sensorData: MutableStateFlow<RawSensorData> = _trillFlex.sensorData
    val sensorData: Flow<RawSensorData> = _sensorData

    init{
        _sensorDataReceiver.waitForData(viewModelScope) { _trillFlex.update(it) }
    }
}