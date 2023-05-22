package com.example.mywearos.presentation.ui.musicplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywearos.data.music.songs
import com.example.mywearos.data.sensor.Scroll
import com.example.mywearos.data.sensor.SensorDataReceiver
import com.example.mywearos.data.sensor.Swipe
import com.example.mywearos.data.sensor.TrillFlexEvent
import com.example.mywearos.data.sensor.TrillFlexSensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

class MusicPlayerViewModel: ViewModel() {
    private val _sensorDataReceiver = SensorDataReceiver()
    private val _trillFlex = TrillFlexSensor()
    private val _trillFlexEvent = _trillFlex.events
    val trillFlexEvent: Flow<TrillFlexEvent> = _trillFlexEvent.filter { it is Scroll || it is Swipe }

    val songList = songs

    init{
        _sensorDataReceiver.waitForData(viewModelScope){ _trillFlex.update(it) }
    }

}