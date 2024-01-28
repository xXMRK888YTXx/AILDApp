package com.xxmrk888ytxx.aildapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.aildapp.domain.RecordRepository
import com.xxmrk888ytxx.aildapp.domain.RecordStateDomainModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecordViewModel(
    private val recordRepository: RecordRepository,
) : ViewModel() {

    val recordUiState = recordRepository.recordState.map {
        it.toUiState()
    }


    fun startRecord() = viewModelScope.launch {
        recordRepository.startRecord()
    }

    fun stopRecord() = viewModelScope.launch {
        recordRepository.stopRecord()
    }

    fun pauseRecord() = viewModelScope.launch {
        recordRepository.pauseRecord()
    }

    fun resumeRecord() = viewModelScope.launch {
        recordRepository.resumeRecord()
    }

    private fun RecordStateDomainModel.toUiState(): RecordStateUiState {
        val timeString = "${recordTime.inWholeMinutes}:${recordTime.inWholeSeconds}"

        return when (this) {
            RecordStateDomainModel.Idle -> RecordStateUiState.Idle
            is RecordStateDomainModel.Pause -> RecordStateUiState.Pause(timeString)
            is RecordStateDomainModel.Recording -> RecordStateUiState.Recording(timeString)
        }
    }

    fun playRecord() = viewModelScope.launch {
        recordRepository.playRecord()
    }
}