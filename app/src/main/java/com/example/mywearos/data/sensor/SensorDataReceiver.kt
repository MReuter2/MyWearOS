package com.example.mywearos.data.sensor

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class SensorDataReceiver{
    private val receivedData = mutableListOf<String>()

    private var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var isBluetoothConnected = false
    private val deviceAddress = "B4:E6:2D:EB:03:2B"

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
            bluetoothSocket?.close()
        }
    }

    fun waitForData(coroutineScope: CoroutineScope, action: (String) -> Unit){
        coroutineScope.launch(Dispatchers.IO) {
            if(connect()){
                while(isActive && isBluetoothConnected){
                    try{
                        val newData = inputStream?.bufferedReader()?.readLine()
                        if (newData != null) {
                            receivedData.add(newData)
                            action(newData)
                            Log.d("DATA", newData)
                        }
                    }catch(_: IOException){
                        Log.d("DATA", "Throws IOException while reading.")
                    }
                }
                disconnect()
            }else{
                Log.d("BLUETOOTH", "Bluetooth connection failed! Unable to read sensor data!")
            }
        }
    }
}