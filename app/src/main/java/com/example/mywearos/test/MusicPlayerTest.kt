package com.example.mywearos.test

import com.example.mywearos.data.songs
import com.example.mywearos.model.MusicPlayer
import junit.framework.TestCase.assertTrue
import org.junit.Test

//TODO Without Livedata or instrumental
class MusicPlayerTest{
    val musicPlayer = MusicPlayer(songs)

    @Test
    fun skipNext(){
        musicPlayer.skipNext()
        val currentSong = musicPlayer.currentSongPlayback.value
        if (currentSong != null) {
            assertTrue(currentSong.equals(songs[1]))
        }
    }
}