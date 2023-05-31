package com.example.mywearos.presentation.ui.contactlist

import androidx.lifecycle.ViewModel
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.Scroll
import com.example.mywearos.data.TrillFlexEvent
import com.example.mywearos.model.TrillFlexSensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter

class ContactListViewModel: ViewModel(){
    private val _trillFlex = TrillFlexSensor()
    private val _trillFlexEvent = _trillFlex.events
    val trillFlexEvent: Flow<TrillFlexEvent> = _trillFlexEvent?.filter { it is Scroll } ?: emptyFlow()

    val contactList = ContactRepo.getContactsSeparatedByFirstLetter()
}