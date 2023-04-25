package com.example.mywearos.data.sensor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.BindException
import java.net.DatagramPacket
import java.net.DatagramSocket

class SensorDataReceiver: ViewModel(){
    var isWaiting = false
    val trillFlexSensor = TrillFlexSensor()

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
            val newRawSensorData = stringToRawSensorData(sensorDataString)
            trillFlexSensor.addRawSensorData(newRawSensorData)
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

// String = {location: 1201, size: 120} , {location: 1201, size: 120} , {location: 1201, size: 120}
fun stringToRawSensorData(string: String): RawSensorData{
    val array = string.split(" , ")
    val locationsWithSize = mutableListOf<Pair<Int,Int>>()
    for(element in array){
        val arraySplittedByLocationAndSize = string.split(",")
        val locationString = arraySplittedByLocationAndSize.first().removePrefix("{location:").trim()
        val sizeString = arraySplittedByLocationAndSize.last().trim().removePrefix("size: ").removeSuffix("}").trim()
        val location = locationString.toIntOrNull()
        val size = sizeString.toIntOrNull()
        if(location != null && size != null){
            locationsWithSize.add(Pair(location, size))
        }
    }
    return RawSensorData(locationsWithSize)
}