package org.example.project.kmmchat.platform

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object DispatcherProvider {
    actual val io: CoroutineDispatcher =  Dispatchers.Default
    actual val main: CoroutineDispatcher = Dispatchers.Main
    actual val default: CoroutineDispatcher = Dispatchers.Default
}