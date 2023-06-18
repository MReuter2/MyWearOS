package com.example.mywearos.model

import androidx.lifecycle.MutableLiveData
import com.example.mywearos.data.PlaybackState
import com.example.mywearos.data.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class SongPlayback(private val songs: List<Song>) {
    val currentSong = MutableLiveData(songs.first())
    private val _timer = Timer()
    val currentTime: StateFlow<Int> = _timer.time
    val playbackState = MutableLiveData(PlaybackState.INITIAL)

    suspend fun play(){
        if(playbackState.value != PlaybackState.RUNNING){
            withContext(Dispatchers.IO) {
                playbackState.postValue(PlaybackState.RUNNING)
                if(playbackState.value == PlaybackState.INITIAL){
                    _timer.start(this)
                    currentTime.collect {
                        if (it >= currentSong.value!!.duration) skipToNextSong()
                    }
                }else
                    if(playbackState.value == PlaybackState.PAUSED){
                        _timer.resume(this)
                        currentTime.collect{
                            if (it >= currentSong.value!!.duration) skipToNextSong()
                        }
                    }
            }
        }
    }

    fun pause(){
        if(playbackState.value == PlaybackState.RUNNING) {
            playbackState.postValue(PlaybackState.PAUSED)
            _timer.pause()
        }
    }

    suspend fun seekTo(time: Int){
        val songDuration = currentSong.value?.duration ?: 0
        if(time < 0)
            _timer.seekTo(0)
        else if(time > (songDuration))
            _timer.seekTo(songDuration)
        else
            _timer.seekTo(time)
    }

    suspend fun skipToNextSong(){
        _timer.seekTo(0)
        currentSong.postValue(getNextSong())
    }

    suspend fun skipToPreviousSong(){
        _timer.seekTo(0)
        currentSong.postValue(getPreviousSong())
    }

    private fun getNextSong(): Song{
        val currentSongIndex = songs.indexOf(currentSong.value)
        if(currentSongIndex == -1)
            return songs.first()
        return if (songs.size > currentSongIndex + 1) {
            songs[currentSongIndex + 1]
        }else{
            songs[0]
        }
    }

    private fun getPreviousSong(): Song{
        val currentSongIndex = songs.indexOf(currentSong.value)
        if(currentSongIndex == -1)
            return songs.first()
        return if(0 > currentSongIndex - 1){
            songs.last()
        }else{
            songs[currentSongIndex - 1]
        }
    }
}