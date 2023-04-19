package com.example.mywearos.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywearos.presentation.ui.sensordata.Touch
import com.example.mywearos.presentation.ui.sensordata.TouchHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.BindException
import java.net.DatagramPacket
import java.net.DatagramSocket

class SensorData: ViewModel(){
    val touchHandler = TouchHandler()
    var isWaiting = false

    init {
        waitForData()
    }

    fun stopWaiting(){
        isWaiting = false
    }

    fun waitForData(){
        isWaiting = true
        viewModelScope.launch(Dispatchers.IO) {
            while(isWaiting) {
                receiveData()
            }
        }
    }
    suspend fun receiveData(){
        val buffer = ByteArray(1024)
        var socket: DatagramSocket? = null
        try {
            socket = withContext(Dispatchers.IO) {
                DatagramSocket(44444)
            }
            socket.broadcast = true
            val packet = DatagramPacket(buffer, buffer.size)
            withContext(Dispatchers.IO) {
                socket.receive(packet)
            }
            val sensorDataString = packet.data.decodeToString().substringAfter("[").substringBefore("]")
            val newTouches = stringToTouches(sensorDataString)
            touchHandler.addTouches(newTouches)
        } catch (e: Exception) {
            println("open fun receiveUDP catch exception.$e")
            if(e is BindException) {
                stopWaiting()
            }
            e.printStackTrace()
        } finally {
            socket?.close()
        }
    }
}

// String = {location: 1201, size: 120}
fun stringToTouch(string: String): Touch?{
    val array = string.split(",")
    val locationString = array.first().removePrefix("{location:").trim()
    val sizeString = array.last().trim().removePrefix("size: ").removeSuffix("}").trim()
    val location = locationString.toIntOrNull()
    val size = sizeString.toIntOrNull()
    if(location == null || size == null)
        return null
    val touch = Touch(location, size)
    return touch
}

// String = {location: 1201, size: 120} , {location: 1201, size: 120} , {location: 1201, size: 120}
fun stringToTouches(string: String): List<Touch>{
    val array = string.split(" , ")
    val touches = mutableListOf<Touch>()
    for(element in array){
        val touch = stringToTouch(element.trim())
        if (touch != null) {
            touches.add(touch)
        }
    }
    return touches
}