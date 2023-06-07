package com.example.mywearos.model

import com.example.mywearos.data.ActionDirection
import com.example.mywearos.data.NoEvent
import com.example.mywearos.data.Scroll
import com.example.mywearos.data.SensorData
import com.example.mywearos.data.Swipe
import com.example.mywearos.data.Touch
import com.example.mywearos.data.TrillFlexEvent
import com.example.mywearos.data.toSensorData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TrillFlexSensorProcessor {
    private val bluetoothReceiver = BluetoothReceiver(
        "B4:E6:2D:EB:03:2B",
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    )
    private var prevSensorDataWithEvent: Pair<SensorData, TrillFlexEvent> = Pair(SensorData(emptyList()), NoEvent())
    val sensorDataWithEvent: Flow<Pair<SensorData, TrillFlexEvent>> = bluetoothReceiver.receiveData().map {
        val sensorData = it.toSensorData()
        val event = identifyNewEvent(sensorData, prevSensorDataWithEvent)
        prevSensorDataWithEvent = Pair(sensorData, event)
        Pair(sensorData, event)
    }

    private fun identifyNewEvent(newSensorData: SensorData,
                                         prevSensorDataWithEvent: Pair<SensorData, TrillFlexEvent>): TrillFlexEvent {
        val locationDifferenceToPreviousData =
                newSensorData.getDifferenceBetweenOtherData(prevSensorDataWithEvent.first)
        val actionDirection =
            if (locationDifferenceToPreviousData.isEmpty() || locationDifferenceToPreviousData.first() == 0)
                ActionDirection.NEUTRAL
            else if (locationDifferenceToPreviousData.first() > 0)
                ActionDirection.NEGATIVE
            else
                ActionDirection.POSITIVE
        val pace =
            if(locationDifferenceToPreviousData.isNotEmpty())
                locationDifferenceToPreviousData.first()
            else
                0

        when(val numberOfFingers = newSensorData.size){
            0 -> return NoEvent()
            1 -> {
                if(pace == 0)
                    return Touch(numberOfFingers)
                return Scroll(actionDirection, pace, numberOfFingers)
            }
            2 -> {
                if(pace == 0)
                    return Touch(numberOfFingers)
                return Scroll(actionDirection, pace, numberOfFingers)
            }
            else -> return Swipe(actionDirection, numberOfFingers)
        }
    }
}