package com.example.mywearos.presentation.ui.musicplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mywearos.data.Scroll
import com.example.mywearos.data.SongPlayback
import com.example.mywearos.data.Swipe
import com.example.mywearos.data.TrillFlexEvent
import com.example.mywearos.data.songs
import com.example.mywearos.model.MusicPlayer
import com.example.mywearos.model.TrillFlexSensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf

//TODO: Implement MusicPlayerState
class MusicPlayerViewModel: ViewModel() {
    private val _trillFlex = TrillFlexSensor()
    private val _trillFlexEvent = _trillFlex.events
    val trillFlexEvent: Flow<TrillFlexEvent> = _trillFlexEvent?.filter { it is Scroll || it is Swipe } ?: flowOf()

    private val _musicPlayer = MusicPlayer(songs)
    private val _currentSongPlayback = _musicPlayer.currentSongPlayback
    val currentSongPlayback: LiveData<SongPlayback> = _currentSongPlayback

    val songList = songs

    fun playStopSong(){

    }

    fun skipPreviousSong(){

    }

    fun skipNextSong(){

    }
}