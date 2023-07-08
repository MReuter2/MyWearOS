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
import kotlin.math.absoluteValue

class ContactListViewModel: ViewModel(){
    private val _trillFlexProcessor = TrillFlexSensorProcessor()
    private val _sensorDataWithEvent = _trillFlexProcessor.sensorDataWithEvent
    val contactList = ContactRepo.getContactsSeparatedByFirstLetter()
    private val _pixelsToScroll = MutableLiveData(0)
    val pixelsToScroll: LiveData<Int> = _pixelsToScroll
    private val _doubleFingerScrollDirection: MutableLiveData<Scroll> = MutableLiveData()
    val doubleFingerScroll: LiveData<Scroll> = _doubleFingerScrollDirection

    init{
        handleTrillFlexEvents()
    }

    private fun handleTrillFlexEvents(){
        viewModelScope.launch(Dispatchers.IO) {
            _sensorDataWithEvent
                .filter{
                    val fit = it.second is Scroll
                    fit
                }
                .map{ it.second }
                .collect{
                    val currentScroll = it as Scroll
                    when(currentScroll.numberOfFingers) {
                        1 -> _pixelsToScroll.postValue(currentScroll.pace)
                        2 -> {
                            if(currentScroll.pace.absoluteValue > 40)
                                _doubleFingerScrollDirection.postValue(currentScroll)
                        }
                    }
            }
        }
    }
}