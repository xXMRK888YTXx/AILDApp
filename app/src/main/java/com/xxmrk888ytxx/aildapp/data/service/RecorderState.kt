package com.xxmrk888ytxx.aildapp.data.service

sealed class RecorderState(open val recordTime: Long) {
    data object Idle : RecorderState(0L)
    data class Recoding(
        override val recordTime: Long,
    ) : RecorderState(recordTime)
    data class Pause(override val recordTime: Long) : RecorderState(recordTime)
}
