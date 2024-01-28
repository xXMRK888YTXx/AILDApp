// IRecordServiceController.aidl
package com.xxmrk888ytxx.aildapp;

import com.xxmrk888ytxx.aildapp.IRecordStateCallback;

interface IRecordServiceController {
    void startRecord(IRecordStateCallback callback);
    void pauseRecord();
    void resumeRecord();
    void stopRecord();
    void playRecord();
}