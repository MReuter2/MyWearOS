package com.example.mywearos.data.sensor

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlin.math.absoluteValue

class TrillFlexSensor {
    val allEvents = mutableStateListOf<TrillFlexEvent>()
    val latestEvent = mutableStateOf<TrillFlexEvent>(NoEvent())

    val allSensorData = mutableStateListOf<RawSensorData>()
    val latestSensorData: RawSensorData?
        get() = if(allSensorData.isNotEmpty()) allSensorData.last() else null

    fun addRawSensorData(newSensorData: RawSensorData){
        val newEvent = identifyNewEventWithSensorData(newSensorData)

        allSensorData.add(newSensorData)
        allEvents.add(newEvent)
        latestEvent.value = newEvent
    }

    private fun identifyNewEventWithSensorData(newSensorData: RawSensorData): TrillFlexEvent{
        var newEvent: TrillFlexEvent = NoEvent()
        val currentLatestSensorData = latestSensorData

        if(currentLatestSensorData != null){
            val locationDifferencesBetweenNewAndOldSensorData =
                getLocationDifferencesBetweenRawSensorData(currentLatestSensorData, newSensorData)
            when (newSensorData.size) {
                1 -> {
                    if (locationDifferencesBetweenNewAndOldSensorData.first().absoluteValue < 4)
                        newEvent = Touch()
                    else
                        if (locationDifferencesBetweenNewAndOldSensorData.first().absoluteValue > 10)
                            newEvent = Scroll(locationDifferencesBetweenNewAndOldSensorData.first())
                }
                2 -> {
                    if(locationDifferencesBetweenNewAndOldSensorData.first() > 10) {
                        val pace = (locationDifferencesBetweenNewAndOldSensorData.first()
                                    + locationDifferencesBetweenNewAndOldSensorData.last()) / 2
                        newEvent = TwoFingerScroll(pace)
                    }
                }
                3, 4, 5 -> {
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

data class RawSensorData(val locationsWithSize: List<Pair<Int, Int>>){
    val size: Int
        get() = locationsWithSize.size
}