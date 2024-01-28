package com.xxmrk888ytxx.aildapp.presentation

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordScreen(
    recordViewModel: RecordViewModel = koinViewModel(),
) {
    val recordState by recordViewModel.recordUiState.collectAsState(RecordStateUiState.Idle)

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = {
            (context as Activity).requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),0)
        }) {
            Text(text = "Request permission")
        }
        Button(onClick = {
            recordViewModel.playRecord()
        }) {
            Text(text = "Play record")
        }
        Text(text = "Duration: ${recordState.recordTimeString}")
        Text(text = "State:${recordState::class.simpleName}")

        if (recordState is RecordStateUiState.Pause) {
            Button(onClick = { recordViewModel.resumeRecord() }) {
                Text(text = "Resume record")
            }
        }

        if (recordState is RecordStateUiState.Recording) {
            Button(onClick = { recordViewModel.pauseRecord() }) {
                Text(text = "Pause record")
            }
        }

        if (recordState is RecordStateUiState.Recording) {
            Button(onClick = { recordViewModel.stopRecord() }) {
                Text(text = "Stop record")
            }
        }

        if (recordState is RecordStateUiState.Idle) {
            Button(onClick = { recordViewModel.startRecord() }) {
                Text(text = "Start record")
            }
        }
    }
}