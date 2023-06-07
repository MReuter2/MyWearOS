package com.example.mywearos.presentation.ui.contactlist

import androidx.lifecycle.ViewModel
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.Scroll
import com.example.mywearos.data.TrillFlexEvent
import com.example.mywearos.model.TrillFlexSensorProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class ContactListViewModel: ViewModel(){
    private val _trillFlexProcessor = TrillFlexSensorProcessor()
    private val _sensorDataWithEvent = _trillFlexProcessor.sensorDataWithEvent
    val trillFlexEvent: Flow<TrillFlexEvent> = _sensorDataWithEvent.filter{ it.second is Scroll }.map{ it.second }

    val contactList = ContactRepo.getContactsSeparatedByFirstLetter()
}