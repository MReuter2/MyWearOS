package com.example.mywearos.presentation.ui.contactlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywearos.data.ContactRepo
import com.example.mywearos.data.Scroll
import com.example.mywearos.model.TrillFlexSensorProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ContactListViewModel: ViewModel(){
    private val _trillFlexProcessor = TrillFlexSensorProcessor()
    private val _sensorDataWithEvent = _trillFlexProcessor.sensorDataWithEvent
    val contactList = ContactRepo.getContactsSeparatedByFirstLetter()
    private val _pixelsToScroll = MutableLiveData(0)
    val pixelsToScroll: LiveData<Int> = _pixelsToScroll
    private val _scrollToNextItem: MutableLiveData<Scroll> = MutableLiveData()
    val scrollToNextItem: LiveData<Scroll> = _scrollToNextItem

    init{
        handleTrillFlexEvents()
    }

    private fun handleTrillFlexEvents(){
        viewModelScope.launch(Dispatchers.IO) {
            _sensorDataWithEvent
                .map{ it.second }
                .filter{ it is Scroll }
                .collect{
                if(it is Scroll){
                    when(it.numberOfFingers) {
                        1 -> _pixelsToScroll.postValue(it.pace)
                        2 -> {
                            if(it.pace > 10)
                                _scrollToNextItem.postValue(it)
                        }
                    }
                }
            }
        }
    }
}