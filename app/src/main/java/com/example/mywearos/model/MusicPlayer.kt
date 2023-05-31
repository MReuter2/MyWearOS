package com.example.mywearos.model

import androidx.lifecycle.MutableLiveData
import com.example.mywearos.data.PlaybackState
import com.example.mywearos.data.Song
import com.example.mywearos.data.SongPlayback

//TODO
class MusicPlayer(private val songs: List<Song>) {
    //TODO: SongPlayback Variable as LiveData/Flow or continuously post a whole SongPlayback every second
    var currentSongPlayback = MutableLiveData(SongPlayback(songs.first(), 0, PlaybackState.STOPPED))

    fun skipNext(){
        val currentPlaybackState = currentSongPlayback.value?.playbackState
        val newSongPlayback =
            SongPlayback(getNextSong(), 0, currentPlaybackState ?: PlaybackState.STOPPED)
        currentSongPlayback.postValue(newSongPlayback)
    }

    fun skipPrevious(){
        val currentPlaybackState = currentSongPlayback.value?.playbackState
        val newSongPlayback =
            SongPlayback(getPreviousSong(), 0, currentPlaybackState ?: PlaybackState.STOPPED)
        currentSongPlayback.postValue(newSongPlayback)
    }

    private fun getNextSong(): Song{
        val currentSong = currentSongPlayback.value?.song
        var nextSongIndex = 0
        if(currentSong != null){
            val currentSongIndex = songs.indexOf(currentSong)
            if (songs.size < currentSongIndex + 1) {
                nextSongIndex = currentSongIndex + 1
            }
        }
        return songs[nextSongIndex]
    }

    private fun getPreviousSong(): Song{
        val currentSong = currentSongPlayback.value?.song
        var previousSongIndex = songs.size - 1
        if(currentSong != null){
            val currentSongIndex = songs.indexOf(currentSong)
            if (0 <= currentSongIndex - 1) {
                previousSongIndex = currentSongIndex - 1
            }
        }
        return songs[previousSongIndex]
    }
}