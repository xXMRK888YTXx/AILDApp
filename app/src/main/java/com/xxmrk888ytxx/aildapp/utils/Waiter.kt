package com.xxmrk888ytxx.aildapp.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking

@JvmInline
value class Waiter(
    private val channel: Channel<Unit> = Channel(0)
) {
    suspend fun doWait() { channel.receive() }
    fun doNotify() { channel.trySend(Unit) }
}