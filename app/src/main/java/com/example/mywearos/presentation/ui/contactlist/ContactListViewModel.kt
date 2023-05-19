package com.example.mywearos.presentation.ui.contactlist

import androidx.lifecycle.ViewModel
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.sensor.Scroll
import com.example.mywearos.data.sensor.SensorDataReceiver
import com.example.mywearos.data.sensor.TrillFlexEvent
import com.example.mywearos.data.sensor.TrillFlexSensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

class ContactListViewModel: ViewModel(){
    private val _sensorDataReceiver = SensorDataReceiver()
    private val _trillFlex = TrillFlexSensor()
    private val _trillFlexEvent = _trillFlex.events
    val trillFlexEvent: Flow<TrillFlexEvent> = _trillFlexEvent.filter { it is Scroll }

    val contactList = ContactRepo.getContactsSeparatedByFirstLetter()

    init {
        _sensorDataReceiver.addObserver(_trillFlex)
    }

    override fun onCleared() {
        _sensorDataReceiver.stopWaitingForData()
        super.onCleared()
    }
}