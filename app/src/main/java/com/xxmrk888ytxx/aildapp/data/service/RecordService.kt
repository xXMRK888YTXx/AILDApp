package com.xxmrk888ytxx.aildapp.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import com.xxmrk888ytxx.aildapp.IRecordServiceController
import com.xxmrk888ytxx.aildapp.IRecordStateCallback
import com.xxmrk888ytxx.aildapp.data.RecordFileProvider
import com.xxmrk888ytxx.aildapp.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class RecordService : Service() {

    private val recorderObserverScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val recordScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var recordCallback: IRecordStateCallback? = null

    private val recordFileProvider by inject<RecordFileProvider>()

    private val recorder by lazy {
        Recorder(applicationContext, recordFileProvider.recordFile, recordScope)
    }

    private val player by lazy { Player(recordFileProvider.recordFile) }

    private val controller = object : IRecordServiceController.Stub() {
        override fun startRecord(callback: IRecordStateCallback) {
            enforceCallingPermission(
                android.Manifest.permission.RECORD_AUDIO,
                "Client haven't record audio permissions"
            )
            recordCallback = callback
            recorder.start()
            startRecorderObserver()
        }

        override fun pauseRecord() {
            recorder.pause()
        }

        override fun resumeRecord() {
            recorder.resume()
        }

        override fun stopRecord() {
            recorder.stop()
        }

        override fun playRecord() {
            //enforceCallingPermission("play_record","Cannot play audio without permission")
            player.play()
        }
    }

    private fun startRecorderObserver() = recorderObserverScope.launch {
        recorder.recordState.collect {
            when (it) {
                RecorderState.Idle -> saveRemoteNotify { onStoped() }
                is RecorderState.Pause -> saveRemoteNotify { onPaused(it.recordTime) }
                is RecorderState.Recoding -> saveRemoteNotify { onRecordingTimeUpdated(it.recordTime) }
            }
            log("Remote record state changed $it", this)
        }
    }

    private fun saveRemoteNotify(doTransact: IRecordStateCallback.() -> Unit) = try {
        recordCallback?.let { doTransact(it) }
    } catch (e: RemoteException) {
        log("saveRemoteNotify exception $e", this)
    }

    override fun onBind(intent: Intent?): IBinder = controller
}