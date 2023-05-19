package com.example.mywearos.data.sensor

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.absoluteValue

class TrillFlexSensor: SensorDataReceiverObserver {
    val events: MutableStateFlow<TrillFlexEvent> = MutableStateFlow(NoEvent())
    private val allEvents = mutableListOf<TrillFlexEvent>(NoEvent())
    val sensorData: MutableStateFlow<RawSensorData> = MutableStateFlow(RawSensorData(emptyList()))

    private fun identifyNewEventWithSensorData(newSensorData: RawSensorData): TrillFlexEvent{
        var newEvent: TrillFlexEvent = NoEvent()
        val latestSensorData = sensorData.value

        val locationDifferencesBetweenNewAndOldSensorData =
            getLocationDifferencesBetweenRawSensorData(latestSensorData, newSensorData)
        val actionDirection = if(locationDifferencesBetweenNewAndOldSensorData.first() < 0)
                ActionDirection.POSITIVE else ActionDirection.NEGATIVE
        val pace = locationDifferencesBetweenNewAndOldSensorData.first().absoluteValue
        val numberOfFingers = latestSensorData.size
        when {
            pace < 4 -> {
                if(allEvents.last() !is Scroll) {
                    newEvent = Touch(numberOfFingers)
                }else{
                    newEvent = Scroll(actionDirection, pace, numberOfFingers)
                }
            }
            pace > 4 -> {
                if(numberOfFingers < 3){
                    newEvent = Scroll(actionDirection, pace, numberOfFingers)
                }else{
                    if(allEvents.last() !is Swipe) {
                        newEvent = Swipe(actionDirection, numberOfFingers)
                    }
                }
            }
        }
        return newEvent
    }

    private fun getLocationDifferencesBetweenRawSensorData(
        rawSensorDataA: RawSensorData, rawSensorDataB: RawSensorData): List<Int>{
        val differences = mutableListOf<Int>()
        val locationsAndSizesA = rawSensorDataA.locationsWithSize
        val locationsAndSizesB = rawSensorDataB.locationsWithSize
        val maxIndex = if(locationsAndSizesA.size <= locationsAndSizesB.size) locationsAndSizesA.size else locationsAndSizesB.size
        locationsAndSizesA.forEachIndexed { index, locationA ->
            if(maxIndex > index){
                val locationB = locationsAndSizesB[index]
                differences.add(locationA.first - locationB.first)
            }
        }
        if(differences.isEmpty())
            differences.add(0)

        return differences
    }

    override fun update(newSensorData: String) {
        val rawSensorData = stringToRawSensorData(newSensorData.substringAfter("[").substringBefore("]"))
        val newEvent = identifyNewEventWithSensorData(rawSensorData)
        Log.d("EVENT", newEvent.toString())
        allEvents.add(newEvent)
        sensorData.value = rawSensorData
        events.value = newEvent
    }
}

data class RawSensorData(val locationsWithSize: List<Pair<Int, Int>>){
    val size: Int
        get() = locationsWithSize.size
}