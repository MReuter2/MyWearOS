package com.example.mywearos.presentation.ui.musicplayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.mywearos.R
import com.example.mywearos.data.ActionDirection
import com.example.mywearos.data.NoEvent
import com.example.mywearos.data.PlaybackState
import com.example.mywearos.data.Scroll
import com.example.mywearos.data.SongPlayback
import com.example.mywearos.data.Swipe
import com.example.mywearos.data.songs
import com.example.mywearos.presentation.theme.MyWearOSTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

@Composable
fun SongScreen(
    musicPlayerViewModel: MusicPlayerViewModel = viewModel()
){
    val songs = musicPlayerViewModel.songList
    val currentSongPlayback by musicPlayerViewModel.currentSongPlayback.observeAsState()
    val latestEvent by musicPlayerViewModel.trillFlexEvent.collectAsStateWithLifecycle(initialValue = NoEvent())
    var currentSong by remember { mutableStateOf(songs.first()) }
    var songIsRunning by remember{ mutableStateOf(false) }
    var currentTime by remember{ mutableStateOf(0) }

    //TODO: Time calculation is model code and not UI code
    //TODO: Events as separate Composable
    //TODO: Move to Viewmodel
    LaunchedEffect(latestEvent){
        when(latestEvent){
            is Swipe ->{
                currentSong = songs.get(songs.indexOf(currentSong) + 1)
                currentTime = 0
            }
            is Scroll ->{
                val timeToSkip =
                    if(latestEvent.actionDirection == ActionDirection.POSITIVE)
                        ((latestEvent as Scroll).pace.toFloat() / 10).roundToInt()
                    else
                        (latestEvent as Scroll).pace / -10
                if(timeToSkip + currentTime >= 0 && timeToSkip + currentTime <= currentSong.duration) {
                    currentTime += timeToSkip
                }else
                    if(timeToSkip + currentTime < 0){
                        currentTime = 0
                    }else{
                        currentTime = currentSong.duration
                    }
            }
            else -> {}
        }
    }
    //TODO: Modelcode
    LaunchedEffect(songIsRunning) {
        while(songIsRunning) {
            delay(1.seconds)
            currentTime += 1
            if(currentTime == currentSong.duration) {
                currentTime = 0
                currentSong = songs.get(songs.indexOf(currentSong) + 1)
            }
        }
    }
    if(currentSongPlayback != null){
        SongScreen(
            currentSongPlayback = currentSongPlayback!!,
            onClickPlayStop = { musicPlayerViewModel.playStopSong() },
            onClickSkipNext = { musicPlayerViewModel.skipNextSong() },
            onClickSkipPrevious = { musicPlayerViewModel.skipPreviousSong() }
        )
    }
}

@Composable
fun SongScreen(
    currentSongPlayback: SongPlayback,
    onClickPlayStop: () -> Unit,
    onClickSkipPrevious: () -> Unit,
    onClickSkipNext: () -> Unit
){
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            SongInfo(
                songTitle = currentSongPlayback.song.title,
                artist = currentSongPlayback.song.artist
            )
            Spacer(modifier = Modifier.size(10.dp))
            SongProgress(
                duration = currentSongPlayback.song.duration,
                progress = currentSongPlayback.progress
            )
            //TODO: Pass events to viewmodel
            StopPlayIcon(
                playbackState = currentSongPlayback.playbackState,
                onClickPlayStop = { onClickPlayStop() },
                onClickSkipPrevious = { onClickSkipPrevious() },
                onClickSkipNext = { onClickSkipNext() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongScreenPreview(){
    MyWearOSTheme {
        SongScreen()
    }
}

@Composable
fun StopPlayIcon(
    playbackState: PlaybackState,
    onClickSkipPrevious: () -> Unit = {},
    onClickPlayStop: () -> Unit = {},
    onClickSkipNext: () -> Unit = {}
){
    Row {
        IconButton(onClick = { onClickSkipPrevious() }) {
            Icon(painter = painterResource(
                id = R.drawable.baseline_skip_previous_24),
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary)
        }
        IconButton(onClick = { onClickPlayStop() }) {
            Icon(painter = painterResource(id =
                    if (playbackState == PlaybackState.RUNNING) R.drawable.baseline_pause_24
                        else R.drawable.baseline_play_arrow_24),
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary)
        }
        IconButton(onClick = { onClickSkipNext() }) {
            Icon(painter = painterResource(
                id = R.drawable.baseline_skip_next_24),
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary)
        }
    }
}

@Preview
@Composable
fun StopPlayIconPreview(){
    var playbackState by remember{ mutableStateOf(PlaybackState.RUNNING) }
    StopPlayIcon(
        playbackState = playbackState,
        onClickPlayStop = {
            playbackState = when(playbackState){
                PlaybackState.RUNNING -> PlaybackState.STOPPED
                PlaybackState.STOPPED -> PlaybackState.RUNNING
            }
        }
    )
}

@Composable
fun SongInfo(songTitle: String, artist: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        Text(text = songTitle, style = MaterialTheme.typography.title2,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(text = artist, style = MaterialTheme.typography.caption2,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Preview
@Composable
fun SongInfoPreview(){
    MyWearOSTheme {
        SongInfo(songTitle = songs.first().title, artist = songs.first().artist)
    }
}

@Composable
fun SongProgress(duration: Int, progress: Int){
    val progressPercentage = progress.toFloat()/duration.toFloat()
    val durationMins = duration / 60
    val durationSecs =
        if(duration - durationMins * 60 < 10) "0${duration - durationMins * 60}"
            else "${duration - durationMins * 60}"
    val restTimeMins = progress / 60
    val restTimeSecs =
        if(progress - restTimeMins * 60 < 10) "0${progress - restTimeMins * 60}"
            else "${progress - restTimeMins * 60}"
    Column(
        modifier = Modifier.width(150.dp)
    ) {
        LinearProgressIndicator(progress = progressPercentage, modifier = Modifier.width(150.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "${restTimeMins}:${restTimeSecs}")
            Text(text = "${durationMins}:${durationSecs}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SongProgressPreview(){
    MyWearOSTheme {
        SongProgress(duration = 320, progress = 40)
    }
}