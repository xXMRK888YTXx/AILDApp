// IRecordStateCallback.aidl
package com.xxmrk888ytxx.aildapp;

interface IRecordStateCallback {
    oneway void onRecordingTimeUpdated(long recordDuractionMills);
    oneway void onPaused(long recordDuractionMills);
    oneway void onStoped();
}