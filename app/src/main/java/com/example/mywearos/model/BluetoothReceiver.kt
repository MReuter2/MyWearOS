package com.example.mywearos.model

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

class BluetoothReceiver(private val addr: String, private val uuid: UUID) {

    fun receiveData(): Flow<String>? {
        val socket = connect() ?: return null
        var isActive = true
        return flow {
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (currentCoroutineContext().isActive && isActive) {
                val bytesRead: Int = withContext(Dispatchers.IO) {
                    var bytes = -1
                    try{
                        bytes = socket.inputStream.read(buffer)
                    }catch (_:IOException){}
                    bytes
                }
                if (bytesRead < 0) break
                Log.d("DATA", buffer.copyOf(bytesRead).decodeToString())
                emit(buffer.copyOf(bytesRead).decodeToString())
            }
        }.onCompletion {
            isActive = false
            socket.close()
        }
    }

    private fun connect(): BluetoothSocket? { //could be suspend for retry policy
        val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(addr) //TODO: deprecated

        return try {
            val socket = device.createInsecureRfcommSocketToServiceRecord(uuid).also {
                it.connect()
                it.outputStream.write("Hello, Im there!".toByteArray())
            }
            socket
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }
    }
}