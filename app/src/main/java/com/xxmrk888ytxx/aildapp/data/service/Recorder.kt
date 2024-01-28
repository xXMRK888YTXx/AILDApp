package com.xxmrk888ytxx.aildapp.data.service

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.CountDownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timerTask

class Recorder(
    private val context: Context,
    private val outputFile: File,
    private val scope: CoroutineScope
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val stopwatchScope = CoroutineScope(Dispatchers.Default.limitedParallelism(1))

    private val _recordState = MutableStateFlow<RecorderState>(RecorderState.Idle)
    val recordState = _recordState.asStateFlow()

    private var mediaRecorder: MediaRecorder? = null

    private val stopwatch = Stopwatch(stopwatchScope)

    @OptIn(ObsoleteCoroutinesApi::class)
    private val commandHandler = scope.actor<RecorderCommand>(
        capacity = BUFFERED
    ) {
        for (command in this) {
            when (command) {
                RecorderCommand.Start -> {
                    mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        MediaRecorder(context)
                    else MediaRecorder()
                    outputFile.delete()
                    mediaRecorder?.let { recorder ->
                        recorder.apply {
                            setAudioSource(MediaRecorder.AudioSource.MIC)
                            setAudioSamplingRate(44100)
                            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                            setOutputFile(outputFile.absolutePath)
                            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
                            setAudioEncodingBitRate(16)
                            prepare()
                            start()
                        }
                    }
                    _recordState.update {
                        RecorderState.Recoding(it.recordTime)
                    }
                    stopwatch.start()
                }

                RecorderCommand.Pause -> {
                    mediaRecorder?.pause()
                    stopwatch.pause()
                    _recordState.update {
                        RecorderState.Pause(it.recordTime)
                    }
                }

                RecorderCommand.Resume -> {
                    mediaRecorder?.resume()
                    stopwatch.resume()
                    _recordState.update {
                        RecorderState.Recoding(it.recordTime)
                    }
                }

                RecorderCommand.Stop -> {
                    mediaRecorder?.apply {
                        stop()
                        reset()
                        release()
                        mediaRecorder = null
                    }
                    _recordState.update {
                        RecorderState.Idle
                    }
                    stopwatch.stop()
                }

                is RecorderCommand.UpdateRecordingDuration -> {
                    val newRecordTime = command.durationMills
                    _recordState.update {
                        when (it) {
                            RecorderState.Idle -> it
                            is RecorderState.Pause -> it.copy(newRecordTime)
                            is RecorderState.Recoding -> it.copy(newRecordTime)
                        }
                    }
                }
            }
        }
    }

    fun start() {
        commandHandler.trySend(RecorderCommand.Start)
    }

    fun pause() {
        commandHandler.trySend(RecorderCommand.Pause)
    }

    fun resume() {
        commandHandler.trySend(RecorderCommand.Resume)
    }

    fun stop() {
        commandHandler.trySend(RecorderCommand.Stop)
    }

    init {
        scope.launch {
            stopwatch.time.collect {
                commandHandler.send(RecorderCommand.UpdateRecordingDuration(it))
            }
        }
    }
}