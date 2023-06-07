package com.example.mywearos.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

//https://git.moxd.io/projects/DREA/repos/ftir-android-app/browse/app/src/main/java/io/moxd/dreair/viewmodel/CountdownViewModel.kt
class Timer {
    private var _job: Job? = null
    private var _currentTime: Int = 0
    private val _time: MutableStateFlow<Int> = MutableStateFlow(0)
    val time: StateFlow<Int>
        get() = _time

    fun start(scope: CoroutineScope){
        if(_job == null){
            _job = scope.launch(Dispatchers.IO) {
                while (isActive) {
                    delay(1000)
                    _currentTime++
                    _time.emit(_currentTime)
                }
            }
        }
    }

    fun pause() {
        _job?.cancel()
        _job = null
    }

    fun resume(scope: CoroutineScope){
        if(_job == null) {
            _job = scope.launch(Dispatchers.IO) {
                while (isActive) {
                    delay(1000)
                    _currentTime++
                    _time.emit(_currentTime)
                }
            }
        }
    }

    suspend fun seekTo(time: Int) {
        _currentTime = time
        _time.emit(_currentTime)
    }
}

fun Int.format(): String {
    val seconds = this % 60
    val secondsString = if(seconds < 10) "0$seconds" else "$seconds"
    val minutes = this / 60
    val minutesString = if(minutes < 10) "0$minutes" else "$minutes"
    return "$minutesString:$secondsString"
}