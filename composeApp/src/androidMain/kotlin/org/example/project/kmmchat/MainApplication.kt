package org.example.project.kmmchat

import android.app.Application
import org.example.project.kmmchat.helper.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }
    }
}