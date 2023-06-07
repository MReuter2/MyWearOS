package com.example.mywearos.presentation.ui.sensordata

import androidx.lifecycle.ViewModel
import com.example.mywearos.data.SensorData
import com.example.mywearos.data.TrillFlexEvent
import com.example.mywearos.model.TrillFlexSensorProcessor
import kotlinx.coroutines.flow.Flow

class SensorDataViewModel: ViewModel() {
    private val _trillFlexProcessor = TrillFlexSensorProcessor()
    private val _sensorDataWithEvent = _trillFlexProcessor.sensorDataWithEvent
    val sensorDataWithEvent: Flow<Pair<SensorData, TrillFlexEvent>> = _sensorDataWithEvent
}