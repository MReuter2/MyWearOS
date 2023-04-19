package com.example.mywearos.presentation.ui.sensordata

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.mywearos.data.SensorData

class SensorDataViewModel(sensorData: SensorData): ViewModel() {
    private val _latestTouches = sensorData.touchHandler.latestTouches
    val latestTouches: SnapshotStateList<Touch>
        get() = _latestTouches

    private val _isScroll = sensorData.touchHandler.isScroll
    val isScroll: State<Boolean>
        get() = _isScroll
    private val _scroll = sensorData.touchHandler.currentScroll
    val scroll: State<Scroll?>
        get() = _scroll
}

class TouchHandler{
    var latestTouches = mutableStateListOf<Touch>()
    val allTouches = mutableListOf<List<Touch>>()
    var isScroll = mutableStateOf(latestTouches.isNotEmpty() && (allTouches.size > 1 && allTouches[allTouches.lastIndex - 1].isNotEmpty()))
    val currentScroll = mutableStateOf<Scroll?>(null)

    fun addTouches(newTouches: List<Touch>){
        isScroll.value = latestTouches.isNotEmpty() && newTouches.isNotEmpty()
        currentScroll.value =
            if(isScroll.value)
                Scroll(
                    calculateScrollPaceWithLocationDifference(
                        getDifferenceBetweenTouches(latestTouches[0], newTouches[0])))
            else null

        latestTouches.removeAll(latestTouches)
        latestTouches.addAll(newTouches)
        allTouches.add(newTouches)
    }

    private fun getDifferenceBetweenTouches(touchA: Touch, touchB: Touch) = touchA.location - touchB.location

    private fun calculateScrollPaceWithLocationDifference(locationDifference: Int): Float{
        val maxDifference = 3712
        return locationDifference.toFloat()/maxDifference
    }
}

//Location: 0-3712, Size: 1-...
class Touch(val location: Int, val size: Int){
    override fun toString(): String {
        return "Location: $location, size: $size"
    }
}

//Pace: 0-1f
data class Scroll(val pace: Float){
    override fun toString(): String {
        return "Pace: $pace"
    }
}