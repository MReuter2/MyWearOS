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
import androidx.wear.compose.material.MaterialTheme.colors
import androidx.wear.compose.material.Text
import com.example.mywearos.R
import com.example.mywearos.data.PlaybackState
import com.example.mywearos.data.Song
import com.example.mywearos.data.songs
import com.example.mywearos.model.format
import com.example.mywearos.presentation.theme.MyWearOSTheme

@Composable
fun SongScreen(
    musicPlayerViewModel: MusicPlayerViewModel = viewModel()
){
    val songs = musicPlayerViewModel.songList
    val currentSong by musicPlayerViewModel.song.observeAsState(songs.first())
    val currentTime by musicPlayerViewModel.currentTime.collectAsStateWithLifecycle(initialValue = 0)
    val currentPlaybackState by musicPlayerViewModel.playbackState.observeAsState(PlaybackState.PAUSED)

    SongScreen(
        song = currentSong,
        playbackState = currentPlaybackState,
        progress = currentTime,
        onClickPlayStop = { musicPlayerViewModel.playStopSong() },
        onClickSkipNext = { musicPlayerViewModel.skipNextSong() },
        onClickSkipPrevious = { musicPlayerViewModel.skipPreviousSong() }
    )
}

@Composable
fun SongScreen(
    song: Song,
    playbackState: PlaybackState,
    progress: Int,
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
                songTitle = song.title,
                artist = song.artist
            )
            Spacer(modifier = Modifier.size(10.dp))
            SongProgress(
                duration = song.duration,
                progress = progress
            )
            StopPlayIcon(
                playbackState = playbackState,
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
                tint = colors.onPrimary)
        }
        IconButton(onClick = { onClickPlayStop() }) {
            Icon(painter = painterResource(id =
                    if (playbackState == PlaybackState.RUNNING) R.drawable.baseline_pause_24
                        else R.drawable.baseline_play_arrow_24),
                contentDescription = null,
                tint = colors.onPrimary)
        }
        IconButton(onClick = { onClickSkipNext() }) {
            Icon(painter = painterResource(
                id = R.drawable.baseline_skip_next_24),
                contentDescription = null,
                tint = colors.onPrimary)
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
                PlaybackState.RUNNING -> PlaybackState.PAUSED
                PlaybackState.PAUSED -> PlaybackState.RUNNING
                PlaybackState.INITIAL -> PlaybackState.RUNNING
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
    Column(
        modifier = Modifier.width(150.dp)
    ) {
        LinearProgressIndicator(progress = progressPercentage, modifier = Modifier.width(150.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = progress.format())
            Text(text =duration.format())
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