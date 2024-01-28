package com.xxmrk888ytxx.aildapp.data.service

import android.content.Context
import android.media.MediaPlayer
import java.io.File

class Player(
    private val file: File,
) {
    private var mediaPlayer: MediaPlayer? = null

    fun play() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.let {
            it.setDataSource(file.absolutePath)
            it.prepare()
            it.start()
        }
    }
}