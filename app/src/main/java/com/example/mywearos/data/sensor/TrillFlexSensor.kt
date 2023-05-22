package com.example.mywearos.data.sensor

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.absoluteValue

class TrillFlexSensor {
    val events: MutableStateFlow<TrillFlexEvent> = MutableStateFlow(NoEvent())
    private val allEvents = mutableListOf<TrillFlexEvent>(NoEvent())
    val sensorData: MutableStateFlow<RawSensorData> = MutableStateFlow(RawSensorData(emptyList()))

    private fun identifyNewEventWithSensorData(newSensorData: RawSensorData): TrillFlexEvent{
        var newEvent: TrillFlexEvent = NoEvent()
        val latestSensorData = sensorData.value

        val latestLocationDifferences =
                latestSensorData.getDifferenceBetweenOtherData(newSensorData)
        val actionDirection = if(latestLocationDifferences.first() < 0)
                ActionDirection.POSITIVE else ActionDirection.NEGATIVE
        val pace = latestLocationDifferences.first().absoluteValue
        val numberOfFingers = latestSensorData.size
        when {
            pace < 4 -> {
                newEvent = if(allEvents.last() !is Scroll) {
                    Touch(numberOfFingers)
                }else{
                    Scroll(actionDirection, pace, numberOfFingers)
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

    fun update(newSensorData: String) {
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

    fun getDifferenceBetweenOtherData(
        otherSensorData: RawSensorData): List<Int>{
        val differences = mutableListOf<Int>()
        val otherLocationAndSize = otherSensorData.locationsWithSize
        val maxIndex = if(this.size <= otherLocationAndSize.size) this.size else otherLocationAndSize.size
        locationsWithSize.forEachIndexed { index, locationA ->
            if(maxIndex > index){
                val locationB = otherLocationAndSize[index]
                differences.add(locationA.first - locationB.first)
            }
        }
        if(differences.isEmpty())
            differences.add(0)

        return differences
    }
}

fun stringToRawSensorData(string: String): RawSensorData{
    val array = string.split(" , ")
    val locationsWithSize = mutableListOf<Pair<Int,Int>>()
    for(element in array){
        val arraySplitByLocationAndSize = string.split(",")
        val locationString = arraySplitByLocationAndSize.first().removePrefix("{location:").trim()
        val sizeString = arraySplitByLocationAndSize.last().trim().removePrefix("size: ").removeSuffix("}").trim()
        val location = locationString.toIntOrNull()
        val size = sizeString.toIntOrNull()
        if(location != null && size != null){
            locationsWithSize.add(Pair(location, size))
        }
    }
    return RawSensorData(locationsWithSize)
}