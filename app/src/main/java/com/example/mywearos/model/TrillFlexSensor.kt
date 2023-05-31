package com.example.mywearos.model

//TODO Deletion
class TrillFlexSensor {
    private val trillFlexSensorProcessor = TrillFlexSensorProcessor()
    val sensorData = trillFlexSensorProcessor.sensorData
    val events = trillFlexSensorProcessor.getEvents
}