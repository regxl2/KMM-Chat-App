package org.example.project.kmmchat.platform

import kotlinx.coroutines.CoroutineDispatcher

expect object DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}