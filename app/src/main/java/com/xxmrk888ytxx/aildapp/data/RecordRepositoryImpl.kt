package com.xxmrk888ytxx.aildapp.data

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.xxmrk888ytxx.aildapp.IRecordServiceController
import com.xxmrk888ytxx.aildapp.IRecordStateCallback
import com.xxmrk888ytxx.aildapp.data.service.RecordService
import com.xxmrk888ytxx.aildapp.domain.RecordException
import com.xxmrk888ytxx.aildapp.domain.RecordRepository
import com.xxmrk888ytxx.aildapp.domain.RecordStateDomainModel
import com.xxmrk888ytxx.aildapp.utils.Waiter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RecordRepositoryImpl(
    private val context: Context
) : RecordRepository, IRecordStateCallback.Stub(), ServiceConnection {

    private val waiter = Waiter()

    private var controller: IRecordServiceController? = null

    private val _recordState = MutableStateFlow<RecordStateDomainModel>(RecordStateDomainModel.Idle)
    override val recordState: Flow<RecordStateDomainModel> = _recordState.asStateFlow()

    override suspend fun startRecord(): Result<Unit> = withServiceController {
        startRecord(this@RecordRepositoryImpl)
    }

    override suspend fun stopRecord(): Result<Unit> = withServiceController { stopRecord() }

    override suspend fun pauseRecord(): Result<Unit> = withServiceController { pauseRecord() }

    override suspend fun resumeRecord(): Result<Unit> = withServiceController { resumeRecord() }

    override suspend fun playRecord() : Result<Unit> = withServiceController { playRecord() }

    override fun onRecordingTimeUpdated(recordDuractionMills: Long) = runBlocking {
        _recordState.emit(
            RecordStateDomainModel.Recording(
                recordDuractionMills.toDuration(
                    DurationUnit.MILLISECONDS
                )
            )
        )
    }

    override fun onPaused(recordDuractionMills: Long) = runBlocking {
        _recordState.emit(
            RecordStateDomainModel.Pause(
                recordDuractionMills.toDuration(
                    DurationUnit.MILLISECONDS
                )
            )
        )
    }

    override fun onStoped() = runBlocking {
        _recordState.emit(RecordStateDomainModel.Idle)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        controller = IRecordServiceController.Stub.asInterface(service)
        waiter.doNotify()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        controller = null
        waiter.doNotify()
    }

    private suspend fun withServiceController(doTransact: suspend IRecordServiceController.() -> Unit): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (controller == null) {
                openConnection()
            }
            val controller = controller
                ?: return@withContext Result.failure<Unit>(RecordException.ImpossibleConnectToServiceException())
            try {
                doTransact(controller)
            } catch (_: RemoteException) {
                return@withContext Result.failure(RecordException.RemoteException())
            }
            return@withContext Result.success(Unit)
        }

    private suspend fun openConnection() {
        context.bindService(
            Intent(context, RecordService::class.java),
            this,
            Context.BIND_AUTO_CREATE
        )
        waiter.doWait()
    }
}