package com.xxmrk888ytxx.aildapp.data

import android.content.Context
import java.io.File

class RecordFileProvider(
    context: Context
) {
    val recordFile = File(context.cacheDir, "record.3gppki")
}