package com.example.mywearos.presentation.ui.contactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.sensor.Scroll
import com.example.mywearos.data.sensor.SensorDataReceiver
import com.example.mywearos.data.sensor.TrillFlexEvent
import com.example.mywearos.data.sensor.TrillFlexSensor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ContactListViewModel: ViewModel(){
    private val _sensorDataReceiver = SensorDataReceiver()
    private val _trillFlex = TrillFlexSensor()
    private val _trillFlexEvent = _trillFlex.events
    val trillFlexEvent: Flow<TrillFlexEvent> = _trillFlexEvent.filter { it is Scroll }

    val contactList = ContactRepo.getContactsSeparatedByFirstLetter()

    init {
        viewModelScope.launch(Dispatchers.IO){
            _sensorDataReceiver.connect()
            while(isActive){
                _sensorDataReceiver.waitForData(this) { _trillFlex.update(it) }
            }
            _sensorDataReceiver.disconnect()
        }
    }
}