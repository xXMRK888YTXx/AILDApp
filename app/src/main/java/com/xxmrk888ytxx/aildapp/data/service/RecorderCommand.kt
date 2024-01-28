package com.xxmrk888ytxx.aildapp.data.service

sealed interface RecorderCommand {
    data object Start : RecorderCommand
    data object Pause : RecorderCommand
    data object Resume : RecorderCommand
    data object Stop : RecorderCommand
    data class UpdateRecordingDuration(
        val durationMills: Long
    ) : RecorderCommand
}
