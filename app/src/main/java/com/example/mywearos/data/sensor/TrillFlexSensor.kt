package com.example.mywearos.data.sensor

import androidx.compose.runtime.mutableStateListOf
import kotlin.math.absoluteValue

class TrillFlexSensor {
    val allEvents = mutableStateListOf<TrillFlexEvent>()
    val allSensorData = mutableStateListOf<RawSensorData>()

    fun addRawSensorData(newSensorData: RawSensorData){
        val newEvent = identifyNewEventWithSensorData(newSensorData)

        allSensorData.add(newSensorData)
        allEvents.add(newEvent)
    }

    private fun identifyNewEventWithSensorData(newSensorData: RawSensorData): TrillFlexEvent{
        var newEvent: TrillFlexEvent = NoEvent()
        val lastEvent: TrillFlexEvent = if(allEvents.isNotEmpty()) allEvents.last() else NoEvent()
        val lastRawSensorData: RawSensorData? = if(allSensorData.isNotEmpty()) allSensorData.last() else null
        if(lastRawSensorData != null){
            val locationDifferencesBetweenNewAndOldSensorData =
                getLocationDifferencesBetweenRawSensorData(lastRawSensorData, newSensorData)
            when (newSensorData.locationsWithSize.size) {
                1 -> {
                    if (locationDifferencesBetweenNewAndOldSensorData.first().absoluteValue < 4)
                        newEvent = Touch()
                    else
                        if (locationDifferencesBetweenNewAndOldSensorData.first().absoluteValue > 10)
                            newEvent = Scroll(locationDifferencesBetweenNewAndOldSensorData.first())
                }

                2, 3, 4, 5 -> {
                    if (locationDifferencesBetweenNewAndOldSensorData.first().absoluteValue < 4)
                        newEvent = Touch()
                    else
                        if (locationDifferencesBetweenNewAndOldSensorData.first().absoluteValue > 4)
                            newEvent = Scroll(locationDifferencesBetweenNewAndOldSensorData.first())
                }
            }
        }else{
            newEvent = Touch()
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
}

data class RawSensorData(val locationsWithSize: List<Pair<Int, Int>>)