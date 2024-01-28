package com.xxmrk888ytxx.aildapp.domain

import kotlinx.coroutines.flow.Flow

interface RecordRepository {
    val recordState: Flow<RecordStateDomainModel>
    suspend fun startRecord(): Result<Unit>
    suspend fun stopRecord(): Result<Unit>
    suspend fun pauseRecord(): Result<Unit>
    suspend fun resumeRecord(): Result<Unit>
    suspend fun playRecord() : Result<Unit>
}