package com.example.mywearos.presentation.ui.sensordata

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.mywearos.data.sensor.RawSensorData
import com.example.mywearos.data.sensor.SensorDataReceiver
import com.example.mywearos.data.sensor.TrillFlexEvent

class SensorDataViewModel(sensorDataReceiver: SensorDataReceiver): ViewModel() {
    private val _allSensorData = sensorDataReceiver.trillFlexSensor.allSensorData
    val allSensorData: SnapshotStateList<RawSensorData>
        get() = _allSensorData

    private val _allTrillFlexEvents = sensorDataReceiver.trillFlexSensor.allEvents
    val allEvents: SnapshotStateList<TrillFlexEvent>
        get() = _allTrillFlexEvents
}