package com.example.mywearos.presentation.ui.musicplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywearos.data.ActionDirection
import com.example.mywearos.data.NoEvent
import com.example.mywearos.data.PlaybackState
import com.example.mywearos.data.Scroll
import com.example.mywearos.data.Song
import com.example.mywearos.data.Swipe
import com.example.mywearos.data.TrillFlexEvent
import com.example.mywearos.data.songs
import com.example.mywearos.model.SongPlayback
import com.example.mywearos.model.TrillFlexSensorProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MusicPlayerViewModel: ViewModel() {
    private val _trillFlex = TrillFlexSensorProcessor()
    private val _songPlayback = SongPlayback(songs)
    private val _song = _songPlayback.currentSong
    val song: LiveData<Song> = _song
    private val _currentTime: StateFlow<Int> = _songPlayback.currentTime
    val currentTime: StateFlow<Int> = _currentTime
    private val _playbackState = _songPlayback.playbackState
    val playbackState: LiveData<PlaybackState> = _playbackState

    val songList = songs

    init {
        handleTrillFlexEvents()
    }

    private fun handleTrillFlexEvents(){
        var latestEvent: TrillFlexEvent = NoEvent()
        viewModelScope.launch(Dispatchers.IO) {
            _trillFlex.sensorDataWithEvent
                .map{ it.second }
                .filter{
                    val fit = (it is Scroll) || (it is Swipe && latestEvent !is Swipe)
                    latestEvent = it
                    fit
                }
                .collect{
                    when(it){
                        is Swipe ->{
                            if(it.actionDirection == ActionDirection.POSITIVE)
                                skipNextSong()
                            else
                                skipPreviousSong()
                        }
                        is Scroll ->{
                            _songPlayback.seekTo((it.pace/10) + _currentTime.value)
                        }
                        else -> {}
                    }
                }
        }
    }

    fun playStopSong(){
        when(_playbackState.value){
            PlaybackState.INITIAL, PlaybackState.PAUSED -> {
                viewModelScope.launch(Dispatchers.IO) { _songPlayback.play() }
            }
            PlaybackState.RUNNING -> _songPlayback.pause()
            else -> {}
        }
    }

    fun skipPreviousSong(){
        viewModelScope.launch(Dispatchers.IO){
            _songPlayback.skipToPreviousSong()
        }
    }

    fun skipNextSong(){
        viewModelScope.launch(Dispatchers.IO){
            _songPlayback.skipToNextSong()
        }
    }
}