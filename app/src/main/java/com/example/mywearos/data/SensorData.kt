package com.example.mywearos.data

data class SensorData(val locationsWithSize: List<Pair<Int, Int>>){
    val size: Int
        get() = locationsWithSize.size

    fun getDifferenceBetweenOtherData(
        otherSensorData: SensorData): List<Int>{
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

fun String.toSensorData(): SensorData{
    val rawStringSeparatedInTouchPositions =
        this.substringAfter("[").substringBefore("]").split(" , ")
    val locationsWithSize = mutableListOf<Pair<Int,Int>>()
    for(touchPosition in rawStringSeparatedInTouchPositions){
        val separateLocationAndSize = touchPosition.split(",")
        val locationString = separateLocationAndSize.first().removePrefix("{\"location\":").trim()
        val sizeString = separateLocationAndSize.last().trim().removePrefix("\"size\": ").removeSuffix("}").trim()
        val location = locationString.toIntOrNull()
        val size = sizeString.toIntOrNull()
        if(location != null && size != null){
            locationsWithSize.add(Pair(location, size))
        }
    }
    return SensorData(locationsWithSize)
}