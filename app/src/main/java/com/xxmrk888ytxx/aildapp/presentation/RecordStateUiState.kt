package com.xxmrk888ytxx.aildapp.presentation

sealed class RecordStateUiState(
    open val recordTimeString: String
) {
    data object Idle : RecordStateUiState(ZERO_DURATION)
    data class Pause(
        override val recordTimeString: String,
    ) : RecordStateUiState(recordTimeString)
    data class Recording(
        override val recordTimeString: String,
    ) : RecordStateUiState(recordTimeString)

    companion object {
        const val ZERO_DURATION = "0:0"
    }
}