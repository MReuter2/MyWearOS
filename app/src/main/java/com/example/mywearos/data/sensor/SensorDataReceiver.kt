package com.example.mywearos.data.sensor

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class SensorDataReceiver: SensorDataReceiverObservable() {
    private val receivedData = mutableListOf<String>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var isWaitingForData = false
    private var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var isBluetoothConnected = false
    private val deviceAddress = "B4:E6:2D:EB:03:2B"

    init {
        waitForData()
    }

    private fun connect(): Boolean{
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        try{
            bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(uuid)
            bluetoothSocket!!.connect()
            inputStream = bluetoothSocket!!.inputStream
            bluetoothSocket!!.outputStream.bufferedWriter().write("Hello, Im there!")
            isBluetoothConnected = true
        }catch (e: IOException){
            e.printStackTrace()
        }catch (e: SecurityException){
            e.printStackTrace()
        }
        return isBluetoothConnected
    }

    private fun disconnect(){
        if (isBluetoothConnected) {
            coroutineScope.cancel()
            bluetoothSocket?.close()
        }
    }

    fun waitForData(action: () -> Unit = {}){
        if(connect()) {
            Log.d("INFO-BT", "Bluetooth is connected")
            isWaitingForData = true
            coroutineScope.launch() {
                while(isWaitingForData) {
                        try{
                            //inputstream
                            val newData = inputStream?.bufferedReader()?.readLine()
                            if (newData != null) {
                                receivedData.add(newData)
                                Log.d("DATA", newData)
                                if (newData != "") {
                                    notify(newData)
                                    action()
                                }
                            }
                        }catch (_: IOException){}
                }
                disconnect()
            }
        }else {
            Log.d("INFO-BT", "Failed Bluetooth connection")
            waitForData()
        }
    }

    fun stopWaitingForData(){
        if(isWaitingForData){
            isWaitingForData = false
        }
    }
}

abstract class SensorDataReceiverObservable{
    private val sensorDataReceiverObserver = mutableListOf<SensorDataReceiverObserver>()
    fun notify(newSensorData: String){
        for(observer in sensorDataReceiverObserver){
            observer.update(newSensorData)
        }
    }

    fun addObserver(newObserver: SensorDataReceiverObserver){
        sensorDataReceiverObserver.add(newObserver)
    }

    fun removeObserver(oldObserver: SensorDataReceiverObserver){
        sensorDataReceiverObserver.remove(oldObserver)
    }
}

interface SensorDataReceiverObserver{
    fun update(newSensorData: String)
}

// String = {location: 1201, size: 120} , {location: 1201, size: 120} , {location: 1201, size: 120}
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