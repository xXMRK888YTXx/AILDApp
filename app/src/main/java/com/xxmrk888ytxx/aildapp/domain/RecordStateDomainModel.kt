package com.xxmrk888ytxx.aildapp.domain

import kotlin.time.Duration

sealed class RecordStateDomainModel(
    open val recordTime: Duration
) {
    data object Idle : RecordStateDomainModel(Duration.ZERO)
    data class Pause(
        override val recordTime: Duration,
    ) : RecordStateDomainModel(recordTime)
    data class Recording(
        override val recordTime: Duration,
    ) : RecordStateDomainModel(recordTime)
}
