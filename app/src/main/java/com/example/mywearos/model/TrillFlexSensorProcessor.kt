package com.example.mywearos.model

import com.example.mywearos.data.ActionDirection
import com.example.mywearos.data.NoEvent
import com.example.mywearos.data.Scroll
import com.example.mywearos.data.SensorData
import com.example.mywearos.data.Swipe
import com.example.mywearos.data.Touch
import com.example.mywearos.data.TrillFlexEvent
import com.example.mywearos.data.toSensorData
import kotlinx.coroutines.flow.map
import java.util.UUID
import kotlin.math.absoluteValue

class TrillFlexSensorProcessor {
    private val bluetoothReceiver = BluetoothReceiver(
        "B4:E6:2D:EB:03:2B",
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    )
    private val allSensorData = mutableListOf<SensorData>()
    //TODO implement sensorDataWithEvent
    val sensorData = bluetoothReceiver.receiveData()?.map {
        val sensorData = it.toSensorData()
        //TODO Remove side effect
        allSensorData.add(sensorData)
        sensorData
    }
    private val allEvents = mutableListOf<TrillFlexEvent>(NoEvent())
    val getEvents = sensorData?.map {
        val event = identifyNewEvent(it)
        //TODO Remove side effect
        allEvents.add(event)
        event
    }

    private fun identifyNewEvent(sensorData: SensorData): TrillFlexEvent {
        val locationDifferenceToPreviousData =
            if(allSensorData.size > 1)
                sensorData.getDifferenceBetweenOtherData(allSensorData.get(allSensorData.lastIndex - 1))
            else
                emptyList()
        val actionDirection =
            if (locationDifferenceToPreviousData.isEmpty() || locationDifferenceToPreviousData.first() == 0)
                ActionDirection.NEUTRAL
            else if (locationDifferenceToPreviousData.first() > 0)
                ActionDirection.NEGATIVE
            else
                ActionDirection.POSITIVE
        val pace =
            if(locationDifferenceToPreviousData.isNotEmpty())
                locationDifferenceToPreviousData.first().absoluteValue
            else
                0

        when(val numberOfFingers = sensorData.size){
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