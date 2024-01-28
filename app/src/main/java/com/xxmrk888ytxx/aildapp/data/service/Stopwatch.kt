package com.xxmrk888ytxx.aildapp.data.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class Stopwatch(
    private val stopWatchScope: CoroutineScope
) {
    private val _time = MutableStateFlow(0L)
    val time = _time.asStateFlow()

    private var stopWatchJob: Job? = null

    fun start() {
        if (stopWatchJob != null) return
        stopWatchJob = createStopWatchJob()
    }

    fun pause() {
        stopWatchJob?.cancel()
        stopWatchJob = null
    }

    fun resume() {
        if(stopWatchJob != null) return
        stopWatchJob = createStopWatchJob()
    }

    fun stop() {
        pause()
        _time.tryEmit(0L)
    }

    private fun createStopWatchJob() : Job = stopWatchScope.launch {
        while (isActive) {
            delay(1000)
            _time.update { it + 1000 }
        }
    }

}