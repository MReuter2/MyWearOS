package com.example.mywearos.sensordata

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket

class SensorDataViewModel: ViewModel(){
    var lastTouches by mutableStateOf("")

    init {
        getNewData()
    }

    private fun getNewData(){
        var waiting = true
        viewModelScope.launch(Dispatchers.IO) {
            while(waiting) {
                waitingForData()
            }
        }
    }

    fun waitingForData(){
            val buffer = ByteArray(1024)
            var socket: DatagramSocket? = null
            try {
                socket = DatagramSocket(44444)
                socket.broadcast = true
                val packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                val sensorDataString = packet.data.decodeToString().substringAfter("[").substringBefore("]")
                lastTouches = sensorDataString
            } catch (e: Exception) {
                println("open fun receiveUDP catch exception." + e.toString())
                e.printStackTrace()
            } finally {
                socket?.close()
            }
    }
}

class Touch(val location: Int?, val size: Int?){
    override fun toString(): String {
        return "Location: $location, size: $size"
    }
}

// String = {location: 1201, size: 120}
fun stringToTouch(string: String): Touch{
    val array = string.split(",")
    val locationString = array.first().removePrefix("{location:").trim()
    val sizeString = array.last().trim().removePrefix("size: ").removeSuffix("}").trim()
    val touch = Touch(locationString.toIntOrNull(), sizeString.toIntOrNull())
    return touch
}

// String = {location: 1201, size: 120} , {location: 1201, size: 120} , {location: 1201, size: 120}
fun stringToTouches(string: String): List<Touch>{
    val array = string.split(" , ")
    val touches = mutableListOf<Touch>()
    for(element in array){
        touches.add(stringToTouch(element.trim()))
    }
    return touches
}